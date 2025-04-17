#include <stdio.h>
#include <stdlib.h>

#ifdef _WIN32
	int java_build(void) {
		int error;
		printf("\nBuilding Java!");
		error = system("cd src & javac -cp \".;Libs/*\" com/vojat/Main.java -d ./Build & cd ..");
		printf("\nBuild compleated!\n");
		return error;
	}
#endif
#ifdef __linux__
	int java_build(void) {
		int error;
		printf("\nBuilding Java!");
		error = system("cd src ; javac -cp \".:Libs/*\" com/vojat/Main.java -d ./Build ; cd ..");
		printf("\nBuild compleated!\n");
		return error;
	}
#endif
