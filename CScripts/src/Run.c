#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define _WIN64_WINNT 0x0500

#ifdef _WIN32
#include <windows.h>
int os = 1;
#endif

#ifdef __linux__
int os = 2;
#endif

void build() {
	int error = 0;
	
	if (os == 1) {
		// Windows
		printf("Building java ...\n");
		error = system("cd src & javac com/vojat/Main.java -d ./Build & cd ..");

		if (error == 0) printf("Build completed\n");
		else printf("Build failed with code %d\n", error);
	} else if (os == 2) {
		// Linux
		printf("Building java ...\n");
		error = system("cd src ; javac com/vojat/Main.java -d ./Build ; cd ..");

		if (error == 0) printf("Build completed\n");
		else printf("Build failed with code %d\n", error);
	}
}

void run() {
	int error = 0;

	if (os == 1) {
		// Windows
		printf("Running java ...\n");
		error = system("cd src/Build & java com/vojat/Main & cd ../..");

		if (error == 0) printf("Program stopped successfully\n");
		else printf("Program stopped with code %d\n", error);
	} else if (os == 2) {
		//Linux
		printf("Running java ...\n");
		error = system("cd src/Build ; java com/vojat/Main ; cd ../..");

		if (error == 0) printf("Program stopped successfully\n");
		else printf("Program stopped with code %d\n", error);
	}
}

int main(int argc, char* argv[]) {

	if (os == 1) {
		if (argc == 2) {
			if (strcmp(argv[1], "-B") == 0) build();
		} else if (argc == 3) {
			if (strcmp(argv[1], "-B") == 0) build();
			if (strcmp(argv[2], "cmd") == 0) {

				// Hides the console window
				HWND hWind = GetConsoleWindow();
				ShowWindow(hWind, SW_MINIMIZE);
				ShowWindow(hWind, SW_HIDE);
			
			}
		} else if (os == 2 && argc == 2 && strcmp(argv[1], "-B") == 0) build();
	}
	run();
	return 0;
}
