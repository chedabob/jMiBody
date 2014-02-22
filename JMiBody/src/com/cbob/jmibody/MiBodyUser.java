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


public class MiBodyUser {

	private int mUserSlot = -1;

	private MiBodyReading[] miBodyReadings = new MiBodyReading[MiBodyFile.NUM_SLOTS_PER_USER];

	public MiBodyUser(int[] raw , int userSlot) {
		
		mUserSlot = userSlot;
		
		int rawCounter = 0;
		for (int nSlotCount = 0; nSlotCount < MiBodyFile.NUM_SLOTS_PER_USER; nSlotCount++) {
			int[] readingData = new int[MiBodyFile.NUM_BYTES_PER_SLOT];

			for (int i = 0; i < readingData.length; i++) {
				readingData[i] = raw[rawCounter++];
			}

			if (MiBodyUtilities.IsRawReadingDataValid(readingData)) {
				MiBodyReading reading = new MiBodyReading(readingData, userSlot);
				miBodyReadings[nSlotCount] = reading;
			}
		}
	}

	public int getUserSlot() {
		return mUserSlot;
	}

	public MiBodyReading [] getReadings () {
		return this.miBodyReadings.clone();
	}
	
	public int getNumberOfReadings () {
		for (int i = 0; i < miBodyReadings.length; i++) {
			if (miBodyReadings[i] == null)
				return i;
		}
		return 0;
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("User slot:");
		builder.append(mUserSlot);
		builder.append('\n');
		
		for (MiBodyReading reading: miBodyReadings)
		{
			builder.append(reading);
			builder.append('\n');
		}
		return builder.toString();
	}
}
