/**
 * OutBreak API - Covid-19
 * Copyright (C) 2020-?XYZ  Steve PECHBERTI <steve.pechberti@laposte.net>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */package fr.covid19.data.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class JNet {

	public static boolean wget(URL _url, Path _path) {
		InputStream     is  = null;
		OutputStream    os  = null;

		int    nb_read = 0;
		byte[] buffer  = new byte[4096];
		try {
			is = _url.openStream();
			os = new FileOutputStream(_path.toFile(), false);

			while((nb_read = is.read(buffer)) > 0)
				os.write(buffer, 0, nb_read);

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
			return false;

		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;

		} finally {

			try { is.close();
			} catch (IOException ioe) { }

			try { os.close();
			} catch (IOException ioe) { return false; }

		}

		return true;
	}

}