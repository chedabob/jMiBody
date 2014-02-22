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

import java.util.Date;

public class MiBodyReading {
	public static int GENDER_MALE = 0;
	public static int GENDER_FEMALE = 1;

	private int[] mRawData;

	private int mGender;
	private int mAge;
	private int mHeight; // CM
	private double mWeight; // KG
	private int mUser;
	private Date mDateTime;
	private double mMuscleMass;
	private double mBodyFat;
	private int mVisceralFat;

	// Not stored in file
	private double mBMI;
	private double mBMR;
	private double mBodyWater;

	
	public MiBodyReading (int [] raw, int userSlot)
	{
		mUser = userSlot;
		this.mRawData = raw;
		mDateTime = MiBodyUtilities.DecodeDateTimeFromBodyReading(this);
    	
        // Step 05: Get Gender

        // convert rawData 7 to a bit array
        Boolean[] ageBits = MiBodyUtilities.byteToBooleanArray((byte)raw[7]);

        // get bit 8 (zero index so is 7!)
        Boolean genderBit = ageBits[7];

        if (genderBit == false)
            mGender = GENDER_FEMALE;
        else
            mGender = GENDER_MALE;

        //  Set gender bit (8) to zero so we can get the age!
        ageBits[7] = false;

        // Step 06: Get Age
        mAge = MiBodyUtilities.GetBitArrayValue(ageBits);

        // Step 07: Get height
        mHeight = raw[8];

        // Step 08: Get weight
        int tmp1 = raw[10];
        int tmp2 = raw[11];
        int result = tmp1 << 8;
        result += tmp2;
        mWeight = ((double)result) / 10;

        // Step 9: Get body fat
        tmp1 = raw[12];
        tmp2 = raw[13];
        result = tmp1 << 8;
        result += tmp2;
        //String bodyFatStr = Integer.toString(result);
        //bodyFatStr = bodyFatStr.insert(bodyFatStr.length() - 1, ".");
        
        mBodyFat = ((double)result) / 10;

        // Step 10: Get Muscle Mass
        tmp1 = raw[15];
        tmp2 = raw[16];
        result = tmp1 << 8;
        result += tmp2;
        mMuscleMass = ((double) result) / 10 ;

        // Step 11: Get Visceral Fat
        mVisceralFat = raw[17];

        // Step 12: Calc BMR
        mBMR = MiBodyUtilities.CalculateBMR(this);

        // Step 13: Calc BMI
        mBMI = MiBodyUtilities.CalculateBMI(this);

        mBodyWater = MiBodyUtilities.CalculateBodyWaterPerc(this);
	}
	
	public int getGender() {
		return mGender;
	}

	public int getAge() {
		return mAge;
	}

	public int getHeight() {
		return mHeight;
	}

	public double getWeight() {
		return mWeight;
	}

	public int getUser() {
		return mUser;
	}

	public Date getDateTime() {
		return mDateTime;
	}

	public double getMuscleMass() {
		return mMuscleMass;
	}

	public double getBodyFat() {
		return mBodyFat;
	}

	public int getVisceralFat() {
		return mVisceralFat;
	}

	public double getBMI() {
		return mBMI;
	}

	public double getBMR() {
		return mBMR;
	}

	public double getBodyWater() {
		return mBodyWater;
	}

	public int[] getRawData() {
		return mRawData;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Gender:");
		builder.append(mGender == GENDER_MALE ? "Male" : "Female");
		builder.append('\n');
		
		builder.append("Age:");
		builder.append(mAge);
		builder.append('\n');
		
		builder.append("Height (CM):");
		builder.append(mHeight);
		builder.append('\n');

		builder.append("Weight (KG):");
		builder.append(mWeight);
		builder.append('\n');
		
		builder.append("User ID:");
		builder.append(mUser);
		builder.append('\n');
		
		builder.append("Reading datetime:");
		builder.append(mDateTime);
		builder.append('\n');

		builder.append("Muscle Mass:");
		builder.append(mMuscleMass);
		builder.append('\n');

		builder.append("Body fat:");
		builder.append(mBodyFat);
		builder.append('\n');

		builder.append("Visceral fat:");
		builder.append(mVisceralFat);
		builder.append('\n');
		
		builder.append("BMI:");
		builder.append(mBMI);
		builder.append('\n');

		builder.append("BMR:");
		builder.append(mBMR);
		builder.append('\n');

		builder.append("Body Water:");
		builder.append(mBodyWater);
		builder.append('\n');

		return builder.toString();
	}
	
}
