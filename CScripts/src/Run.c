#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define _WIN64_WINNT 0x0500
#include <windows.h>

int main(int argc, char* argv[]) {

	if (argc > 1 && ( strcmp(argv[1], "cmd") == 0 )) {

		printf("Running GardeningGame\n");
		system("cd src/build & java.exe com/vojat/Main & cd ../../");

	} else {

		// Hides the console window
		HWND hWind = GetConsoleWindow();
		ShowWindow(hWind, SW_MINIMIZE);
		ShowWindow(hWind, SW_HIDE);

		system("cd src/build & java.exe com/vojat/Main & cd ../../");
	
	}

	// Test for integrated java path: "./../../res/jdk-17/bin/java.exe"

	return 0;
}
