/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
#include <windows.h>
#include <shellapi.h>

void getPath(char *result, char* lpCmdLine);

int APIENTRY WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int  nCmdShow){

	/*strip " and g2gui.exe from commandLine and store to path */

	char pathToExe[strlen(GetCommandLine())];
	getPath(pathToExe,lpCmdLine);


	char* executable = "javaw.exe\0";
	char* javaArguments1 = "-Djava.library.path=\0";
	char* javaArguments2 = "lib -jar \0";
	char* pathToJar = "lib\\g2gui.jar \0";

	/*create a string, that is long enough for all the strings*/
	char arguments[(strlen(lpCmdLine)+strlen(javaArguments1)+2*strlen(pathToExe)+strlen(pathToJar)+strlen(javaArguments2))+1];

	/*copy all the strings together, bulding the right sequence*/
	strcat(arguments, javaArguments1);
	strcat(arguments,pathToExe);
	strcat(arguments, javaArguments2);
	strcat(arguments, pathToExe);
	strcat(arguments, pathToJar);
	strcat(arguments, lpCmdLine);


	/*
	* execute the Java VM with corresponding arguments, MessageBox if javaw.exe
	* is not found or fails for other reasons
	*/
	if(ShellExecute(NULL,NULL,executable,arguments,".",SW_SHOWNORMAL)<=(HINSTANCE)32)
	{
		MessageBox(NULL,
			"Could not find JRE!\n\n Download from: http://www.java.com",
			"G2Gui error",MB_OK | MB_ICONASTERISK);
		return 2;
	}

	return 0;
}

/* gets the commandline the exe was started from, and extract therefrom the path*/
void getPath(char *result, char* lpCmdLine){

	char* constCommandLine = GetCommandLine();
	char commandLine[strlen(constCommandLine)+1];
	strcpy(commandLine,constCommandLine);

	// Remove lpCmdLine from back
	int lastPosition = strlen(commandLine)-strlen(lpCmdLine);

	/*searches for g2gui from behind and since this is the exe, we discard
	 *it, we are only interested in path to g2gui.exe
	 */
	while(commandLine[lastPosition]!='i')
	{
		if (commandLine[lastPosition--]=='u')
		if (commandLine[lastPosition--]=='g')
		if (commandLine[lastPosition--]=='2')
		if (commandLine[lastPosition--]=='g'){
			lastPosition++;
			break;
		}

		lastPosition--;
	}

	commandLine[lastPosition] = '\0';

	int firstPosition = 0;
	while (commandLine[firstPosition]=='\"' || commandLine[firstPosition]==' '){
		firstPosition++;
	}

	strcpy(&result[0], &commandLine[firstPosition]);

	int resultlength = strlen(result);
	result[resultlength+1] = '\0';
}
