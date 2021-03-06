package info.kgeorgiy.java.advanced.implementor;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Impler {
    /**
     * Produces code implementing class or interface specified by provided <var>token</var>.
     * <p>
     * Generated class classes name should be same as classes name of the type token with <var>Impl</var> suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * <var>root</var> directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to <var>$root/java/util/ListImpl.java</var>
     *
     *
     * @param token type token to create implementation for.
     * @param root root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
     * generated.
     */
    /*
    java -cp . -p . -m info.kgeorgiy.java.advanced.implementor interface info.kgeorgiy.ja.istratov.implementor.Implementor
    java -cp . -p . -m info.kgeorgiy.java.advanced.implementor jar-interface info.kgeorgiy.ja.istratov.implementor.Implementor
    info.kgeorgiy.ja.istratov.implementor.TestInterface test02_methodlessInterfaces\info.kgeorgiy.ja.istratov.implementor.TestInterface.jar
    info.kgeorgiy.ja.istratov.implementor.TestInterface testRoot
    */
    void implement(Class<?> token, Path root) throws ImplerException;
}
