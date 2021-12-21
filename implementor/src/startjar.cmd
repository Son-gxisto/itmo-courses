SET rep=.\..\..\java-advanced-2021
SET lib=%rep%\lib\*
SET modules=%rep%\modules
SET artifacts=%rep%\artifacts
SET kgJarImpler=%artifacts%\info.kgeorgiy.java.advanced.implementor.jar
SET inJarImplementor=info\kgeorgiy\java\advanced\implementor\

javac -cp %kgJarImpler%;%lib%;%modules%; info\kgeorgiy\ja\istratov\implementor\Implementor.java

jar xf %kgJarImpler% %inJarImplementor%Impler.class %inJarImplementor%JarImpler.class %inJarImplementor%ImplerException.class
jar -cmf manifest.mf implementor.jar info\kgeorgiy\ja\istratov\implementor\*.class %inJarImplementor%*.class