/* Copyright 2014 chedabob
 * Derived from code by flippydeflippydebop - http://code.google.com/p/opensaltermibody
 * 
 * This file is part of JMiBody.
 * 
 * JMiBody is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JMiBody is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JMiBody.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cbob.jmibody;

public class MiBodyFile {

	public static final int NUM_USERS = 12;
	public static final int NUM_SLOTS_PER_USER = 35;
	public static final int NUM_BYTES_PER_SLOT = 18;

	private MiBodyUser[] users = new MiBodyUser[NUM_USERS];

	public MiBodyUser[] getUsers() {
		return users.clone();
	}

	public MiBodyFile(byte[] raw) {
		// There are 12 users in the system. Each user has a maximum of 35
		// slots. Each slot contains the data.
		// This data is made up of 18 bytes. Read each of them in to MiBodyData
		// class.

		int nFileContentsCounter = 0;
		for (int nUserCount = 0; nUserCount < NUM_USERS; nUserCount++) {

			int[] temp = new int[NUM_BYTES_PER_SLOT * NUM_SLOTS_PER_USER];

			for (int nByteCount = 0; nByteCount < temp.length; nByteCount++) {
				temp[nByteCount] = raw[nFileContentsCounter++] & 0xFF;
			}
			
			users[nUserCount] = new MiBodyUser(temp, nUserCount + 1);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (MiBodyUser user: users)
		{
			builder.append(user);
			builder.append('\n');
		}
		return builder.toString();
	}
}
