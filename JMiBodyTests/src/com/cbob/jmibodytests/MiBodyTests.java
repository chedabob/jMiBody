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

package com.cbob.jmibodytests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


import org.junit.Before;
import org.junit.Test;

import com.cbob.jmibody.*;

public class MiBodyTests {

	private static MiBodyFile mFile;
	private static byte[] mRawBytes;

	@Before
	public void setup() throws IOException {
		URL url = getClass().getResource("/BODYDATA.txt");
		File f = new File(url.getFile());
		mRawBytes = toByteArray(f);
		mFile = new MiBodyFile(mRawBytes);
	}

	@Test
	public void testFileLoad() {
		assertNotNull(mRawBytes);
	}

	@Test
	public void testInitialParse() {
		assertNotNull(mFile);
	}

	@Test
	public void testUser1Read() {
		assertNotNull(mFile.getUsers()[0]);
	}

	@Test
	public void testUser1Has6Readings() {
		MiBodyUser user = mFile.getUsers()[0];
		assertEquals(6, user.getNumberOfReadings());
	}
	
	@Test 
	public void testUser2Has0Readings () {
		MiBodyUser user = mFile.getUsers()[1];
		assertEquals(0, user.getNumberOfReadings());
	}

	@Test
	public void testHeightSet() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[0];
		assertEquals(174, reading.getHeight());
	}

	@Test
	public void testAgeSet() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[5];
		assertEquals(22, reading.getAge());
	}

	@Test
	public void testWeightSetSlot3() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[2];
		assertEquals(54.3f, reading.getWeight(), 0.1f);
	}

	@Test
	public void testWeightSetSlot1 () {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[0];
		assertEquals(53.5f, reading.getWeight(), 0.1f);
	}

	@Test
	public void testBodyFatSet() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[3];
		assertEquals(8.4f, reading.getBodyFat(), 0.1f);
	}

	@Test
	public void testVisceralFatSet() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[3];
		assertEquals(1, reading.getVisceralFat());
	}

	@Test
	public void testBMICorrect() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[5];
		assertEquals(19.3f, reading.getBMI(), 0.1f);
	}

	@Test
	public void testBMRCorrect() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[5];
		assertEquals(1571.89f, reading.getBMR(), 0.1f);
	}
	
	@Test
	public void testMuscleMassCorrect() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[4];
		assertEquals(51.0f, reading.getMuscleMass(), 0.1f);
	}
	
	@Test
	public void testBodyWaterCorrect() {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[2];
		assertEquals(69.5f, reading.getBodyWater(), 0.5f);
	}
	
	@Test
	public void testUserSlotSet () {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[2];
		
		assertEquals(user.getUserSlot(),reading.getUser());
	}
	
	@Test
	public void testDateTimeCorrect () throws ParseException {
		MiBodyUser user = mFile.getUsers()[0];
		MiBodyReading reading = user.getReadings()[1];
		
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy", Locale.ENGLISH);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date expected = format.parse("20:09:47 11/05/2011");
		Date actual = reading.getDateTime();
		assertTrue(actual.equals(expected));
	}
	
	private static byte[] toByteArray(File file) throws IOException {
		int length = (int) file.length();
		byte[] array = new byte[length];
		InputStream in = new FileInputStream(file);
		int offset = 0;
		while (offset < length) {
			in.read(array, offset, (length - offset));
			offset += length;
		}
		in.close();

		return array;
	}

}
