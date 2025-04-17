#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char* argv[]) {
    // Create the icon .res files
    system("windres src/RunIcon.rc -O coff -o RunIcon.res");
    
    // Build and remove the the .res files
    system("gcc src/Run.c RunIcon.res -o ../GardeningGame.exe");

    // Check for the verbose flag
    if (argc == 1) {

        // removes the .res files
        printf("Deleting the .res files\n");
        system("del RunIcon.res");

    } else if (strcmp(argv[1], "-v") == 0) {

        printf("Verbose mode does not delete the .res files\n");
        return 0;

    }

    return 0;
}