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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MiBodyUtilities {

	public static byte GetBitArrayValue(Boolean[] bArray) {
		byte value = 0x00;

		for (byte x = 0; x < bArray.length; x++) {
			value |= (byte) ((bArray[x] == true) ? (0x01 << x) : 0x00);
		}

		return value;
	}

	public static byte LowByte(char word) {
		return (byte) word;
	}

	static public byte HighByte(char word) {
		return ((byte) (((char) (word) >> 8) & 0xFF));
	}

	public static char GetBits(byte value, int startbit, int bitcount) {
		char result;

		result = (char) (value << (startbit - 1));

		result = (char) (result >> (8 - bitcount));

		return result;
	}

	public static double roundTo2dp(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
	
	public static double roundTo1dp(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.#");
		return Double.valueOf(twoDForm.format(d));
	}

	public static Boolean[] byteToBooleanArray(byte input) {
		Boolean[] output = new Boolean[8];

		for (int i = 0; i < 8; i++) {
			byte curByte = 1;

			output[i] = ((curByte << i) & input) == 0 ? false : true;
		}

		return output;
	}

	public static double CalculateBodyWaterPerc(MiBodyReading bd) {
		double muscleMass = bd.getMuscleMass() * bd.getWeight() / 100;

		double fatMass = bd.getBodyFat() * bd.getWeight() / 100;

		double restOfFluids = bd.getWeight() - muscleMass - fatMass;

		double waterMass = muscleMass * 0.83 + restOfFluids * 0.62;

		double waterPerc = waterMass / bd.getWeight() * 100;

		return roundTo1dp(waterPerc);
	}

	public static double CalculateBMI(MiBodyReading bd) {
		// convert height from cm to metres
		double heightInMetres = bd.getHeight() * 0.01;

		double tmp = heightInMetres * heightInMetres;

		double bmi = bd.getWeight() / tmp;

		return roundTo1dp(bmi);

		// 1.6 x 1.6 = 2.56. BMI would be 65 divided by 2.56 = 25.39.
		// calc taken from here:
		// http://www.bbc.co.uk/health/healthy_living/your_weight/bmiimperial_index.shtml
	}

	public static double CalculateBMR(MiBodyReading bd) {
		// calc taken from here:
		// http://www.bmi-calculator.net/bmr-calculator/bmr-formula.php

		double BMR = 0;
		if (bd.getGender() == MiBodyReading.GENDER_FEMALE) {
			// Women: BMR = 655 + ( 9.6 x weight in kilos ) + ( 1.8 x height in
			// cm ) - ( 4.7 x age in years )
			BMR = 655 + (9.6 * bd.getWeight()) + (1.8 * bd.getHeight())
					- (4.7 * bd.getAge());
		} else {
			// Men: BMR = 66 + ( 13.7 x weight in kilos ) + ( 5 x height in cm )
			// - ( 6.8 x age in years )
			BMR = 66 + (13.7 * bd.getWeight()) + (5 * bd.getHeight())
					- (6.8 * bd.getAge());
		}

		return BMR;
	}
	

    public static Date DecodeDateTimeFromBodyReading(MiBodyReading bodyReading)
    {
        // Step 01: Get Year
        int year = bodyReading.getRawData()[0] << 8;
        year += bodyReading.getRawData()[1];

        // Step 02: Get Month
        int month = bodyReading.getRawData()[2];

        // Step 03: Get Day
        int day = bodyReading.getRawData()[3];

        // Step 04: Get Time 
        int hour = bodyReading.getRawData()[4];
        int min = bodyReading.getRawData()[5];
        int sec = bodyReading.getRawData()[6];

        
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, sec);
        c.clear(Calendar.MILLISECOND);

        return c.getTime();
    }
    
    public static Boolean IsRawReadingDataValid(int [] raw)
    {
        int nonZeroCount = 0;
        for (int b : raw)
        {
            if (b != 0)
                nonZeroCount++;
        }

        return nonZeroCount >= 5;
    }
}