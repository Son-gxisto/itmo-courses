package info.kgeorgiy.ja.istratov.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;


/**
 * Implement {@link JarImpler}.
 * Make implementation of Interfaces
 */
public class Implementor implements JarImpler {
    /**
     * Delete temp files
     */
    private static final SimpleFileVisitor<Path> DELETE_VISITOR = new SimpleFileVisitor<>() {
        /**
         * Delete given file.
         * @param file is a file for deleting
         * @param attrs not really used for this implementation
         * @return {@link FileVisitResult#CONTINUE}
         * @throws IOException from {@link Files#delete}
         */
        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        /**
         * Delete given directory
         * @param dir is a directory for deleting
         * @param exc is an Exception
         * @return {@link FileVisitResult#CONTINUE}
         * @throws IOException from {@link Files#delete}
         */
        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    /**
     * Return correct 0 value of given class on string implementation,
     * or empty string for void;
     * @param clazz is Class
     * @return correct 0 value of Class or empty string;
     */
    private static String getDefaultValue(final Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return "null";
            // :NOTE: .equals не нужен
        } else if (clazz == boolean.class) {
            return "false";
        } else if (clazz == void.class) {
            return "";
        } else {
            return "0";
        }
    }

    /**
     * Encode string to unicode
     * @param s is a string for encoding
     * @return unicode encoding of s
     */
    static String toUnicode(final String s) {
        return s.chars().mapToObj(c -> {
            if (c < 128) {
                return String.valueOf((char) c);
            } else {
                return String.format("\\u%04X", c);
            }
        }).collect(Collectors.joining());
    }

    /**
     * Make a String implementation of method
     * @param method is a method for implementation
     * @return String implementation of method
     */
    private static String generateMethod(final Method method) {
        if (method.isDefault() ||
                Modifier.isProtected(method.getModifiers()) ||
                Modifier.isStatic(method.getModifiers())) {
            return "";
        }
        return String.format("    public %s %s (%s) {\n        return %s;\n    }\n",
                method.getReturnType().getCanonicalName(),
                method.getName(),
                Arrays.stream(method.getParameters())
                        .map(parameter -> parameter.getType().getCanonicalName() + " " + parameter.getName())
                        .collect(Collectors.joining(", ")),
                getDefaultValue(method.getReturnType()));
    }

    /** Write all methods of class
     * @param writer is a writer for output
     * @param clazz is a class that implement
     *              // :NOTE: JD
     * @throws IOException from {@link BufferedWriter#write}
     */
    private void writeMethods(final BufferedWriter writer, final Class<?> clazz) throws IOException {
        writer.write(toUnicode(Arrays.stream(clazz.getMethods())
                .map(Implementor::generateMethod).collect(Collectors.joining())));
    }

    /**
     * Create parent directory of path
     * @param path is a path for creating directory
     * @throws ImplerException, which is wrapper IOException of {@link Files#createDirectories(Path, FileAttribute[])};
     */
    private static void createParent(final Path path) throws ImplerException {
        if (path.getParent() != null) {
            try {
                Files.createDirectories(path.getParent());
            } catch (final IOException e) {
                throw new ImplerException(e.getMessage());
            }
        }
    }

    /**
     * Make a path of (class file + Impl + suffix)
     * @param clazz is a class for concat.
     * @param path is a target directory
     * @param suffix is a suffix
     * @return path of (class file + Impl + suffix)
     */
    private Path getPathSuffix(final Class<?> clazz, final Path path, final String suffix) {
        return path.resolve(clazz.getPackageName().replace(".", File.separator) +
                File.separator + clazz.getSimpleName() + "Impl" + suffix);
    }

    /**
     * Produces code implementing class or interface specified by provided <var>token</var>.
     * Generated class classes name should be same as classes name of the type token with <var>Impl</var> suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * <var>root</var> directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to <var>$root/java/util/ListImpl.java</var>
     * @param token type token to create implementation for.
     * @param root root directory.
     * @throws ImplerException
     * if: <ul>
     *     <li>token is not an public interface</li>
     *     <li>Writing failed (Wrap of {@link IOException})</li>
     * </ul>
     */
    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        if (!token.isInterface() || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("You can't create class from " + token.getName());
        }
        final Path path = getPathSuffix(token, root, ".java");
        createParent(path);

        try (final BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            if (!token.getPackageName().isEmpty()) {
                writer.write(toUnicode(String.format("package %s;\n\n", token.getPackageName())));
            }
            writer.write(toUnicode(String.format(
                    "public class %sImpl implements %s {\n",
                    token.getSimpleName(),
                    token.getCanonicalName()
            )));
            writeMethods(writer, token);
            writer.write("}");
        } catch (final IOException e) {
            throw new ImplerException("Implementing error:" + e.getMessage());
        }
    }

    /**
     * Delete files in directory, used for delete temp files.
     * @param path is a directory
     * @throws IOException from {@link Files#walkFileTree(Path, FileVisitor)}
     */
    private void clean(final Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, DELETE_VISITOR);
        }
    }

    /**
     * Get a path of class
     * @param token is a class
     * @return path of class
     * @throws ImplerException, which is a wrapper for {@link URISyntaxException}
     */
    private static String getClassPath(final Class<?> token) throws ImplerException {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new ImplerException(e.getMessage());
        }
    }

    /**
     * Compile implemented class
     * @param clazz is a implemented class
     * @param path is a target directory
     * @throws ImplerException when compilation error.
     */
    private void compile(final Class<?> clazz, final Path path) throws ImplerException {
        final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        final String s = getPathSuffix(clazz, path, ".java").toString();
        if (javaCompiler.run(null, null, null, "-encoding", "utf8", "-cp", getClassPath(clazz), s) != 0) {
            throw new ImplerException("Compilation error for:" + getPathSuffix(clazz, path, ".java"));
        }
    }

    /**
     * Make a jar compilation of compiled class
     * @param token type token to create implementation for.
     * @param jarFile target <var>.jar</var> file.
     * @throws ImplerException {@link ImplerException},
     * which is wrapper for {@link IOException}
     * if <ul>
     *     <li>Creating of temp directory failed</li>
     *     <li>{@link #implement} throws an exception</li>
     *     <li>{@link #compile} throws an exception</li>
     * </ul>
     */
    @Override
    public void implementJar(final Class<?> token, final Path jarFile) throws ImplerException {
        createParent(jarFile);
        final Path temp;
        try {
            temp = Files.createTempDirectory(jarFile.toAbsolutePath().getParent(), "temp");
            implement(token, temp);
            compile(token, temp);
        } catch (final IOException | ImplerException e) {
            throw new ImplerException("implementation or compilation error:" + e.getMessage());
        }
        final Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        final String classFileName = token.getPackageName().replace('.', '/') + "/" + token.getSimpleName() + "Impl.class";
        try (final JarOutputStream jar = new JarOutputStream(Files.newOutputStream(jarFile), manifest)){
            jar.putNextEntry(new ZipEntry(classFileName));
            Files.copy(getPathSuffix(token, temp, ".class"), jar);
        } catch (final IOException e) {
            throw new ImplerException("jar compilation error:" + e.getMessage());
        } finally {
            try {
                clean(temp);
            } catch (final IOException e) {
                System.err.println("Error while deleting temp directory:" + e.getMessage());
            }
        }
    }

    /**
     * // :NOTE: JD
     * Make implementation of given {@link Class}
     * make .class implementation if has 2 arguments
     * make .jar implementation if started with "-jar" and has 3 arguments
     * @param args: -jar Optionally, interface class name, target directory || jar file name
     */
    public static void main(final String[] args) {
        if (args == null || !(args.length == 2 || args.length == 3) || (args.length == 3 && !args[0].equals("-jar"))) {
            System.err.println("usage: \"-jar\" class name, target directory | jarFileName\n You write:" + Arrays.toString(args));
            return;
        }
        try {
            final Implementor impler = new Implementor();
            if (args.length == 2) {
                impler.implement(Class.forName(args[0]), Paths.get(args[1]));
            } else {
                impler.implementJar(Class.forName(args[1]), Paths.get(args[2]));
            }
        } catch (final ClassNotFoundException e) {
            System.err.println("Class not found:" + ("-jar".equals(args[0]) ? args[1] : args[0]));
        } catch (ImplerException e) {
            System.err.println(e.getMessage());
        }
    }
}
