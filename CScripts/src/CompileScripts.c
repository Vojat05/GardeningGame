#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char* argv[]) {
    // Create the icon .res files
    system("windres src/RunIcon.rc -O coff -o RunIcon.res");
    system("windres src/BuildIcon.rc -O coff -o BuildIcon.res");
    
    // Build and remove the the .res files
    system("gcc src/Run.c RunIcon.res -o ../GardeningGame.exe");
    system("gcc src/Build.c BuildIcon.res -o ../Build.exe");

    // Check for the verbose flag
    if (argc == 1) {

        // removes the .res files
        printf("Deleting the .res files");
        system("del RunIcon.res");
        system("del BuildIcon.res");

    } else if (strcmp(argv[1], "-v") == 0) {

        printf("Verbose mode does not delete the .res files");
        return 0;

    }

    return 0;
}