#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "Build.c"

#define _WIN64_WINNT 0x500

#ifdef _WIN32
	#include <windows.h>
	int build() {
		int error = 0;

		// Windows
		printf("Building java ...\n");
		error = java_build();

		if (error == 0) printf("Build completed\n");
		else printf("Build failed with code %d\n", error);
		return error;
	}

	int run() {
		int error = 0;

		// Windows
		printf("Running java ...\n");
		error = system("cd src/Build & java -cp \"../../out;../../src/Libs/lwjgl-lib/*;.\" com.vojat.Main & cd ../..");

		if (error == 0) printf("Program stopped successfully\n");
		else printf("Program stopped with code %d\n", error);
		return error;
	}

	void hideConsole(void) {
		// Hides the console window
		HWND hWind = GetConsoleWindow();
		ShowWindow(hWind, SW_MINIMIZE);
		ShowWindow(hWind, SW_HIDE);
	}
#endif

#ifdef __linux__
	int build() {
		int error = 0;

		// Linux
		printf("Building java ...\n");
		error = java_build();

		if (error == 0) printf("Build completed\n");
		else printf("Build failed with code %d\n", error);
		return error;
	}

	int run() {
		int error = 0;

		//Linux
		printf("Running java ...\n");
		error = system("cd src/Build ; java com/vojat/Main ; cd ../..");

		if (error == 0) printf("Program stopped successfully\n");
		else printf("Program stopped with code %d\n", error);
		return error;
	}

	void hideConsole(void) {}
#endif

int main(int argc, char* argv[]) {

	if (argc == 2 && strcmp(argv[1], "-B") == 0) build();
	else if (argc == 3) {
		if (strcmp(argv[1], "-B") == 0) build();
		if (strcmp(argv[2], "cmd") == 0) hideConsole();
	}

	// Run the java application
	run();
	return 0;
}
