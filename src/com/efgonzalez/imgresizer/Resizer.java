package com.efgonzalez.imgresizer;

import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import org.imgscalr.*;

public class Resizer extends SwingWorker<Void, Void> {

	private Set<String> files;
	private int width;
	private int height;
	private String output;

	public Resizer(String input, int width, int height, String output) {
		this.files = loadFiles(input);
		this.width = width;
		this.height = height;
		this.output = output;
	}

	public Set<String> loadFiles(String text) {
		Set<String> fileList = new LinkedHashSet<String>();
		String[] pathlist = text.split("\\r?\\n");
		for (String s : pathlist) {
			File tmp = new File(s);
			if (tmp.isDirectory()) {
				String[] dirFiles = tmp.list();
				for (String filepath : dirFiles) {
					try {
						if (filepath.toLowerCase().endsWith(".jpeg")
								| filepath.toLowerCase().endsWith(".jpg"))
							fileList.add(tmp.getCanonicalPath()
									+ File.separator + filepath);
						if (filepath.toLowerCase().endsWith(".png"))
							fileList.add(tmp.getCanonicalPath()
									+ File.separator + filepath);
					} catch (java.io.IOException e) {
					}
				}
			} else
				fileList.add(s);
		}
		return fileList;
	}

	public void resizeFiles() {
		File outputdir = new File(output);

		if (!outputdir.exists())
			outputdir.mkdir();

		int numFiles = files.size();
		int i = 1;
		for (String s : files) {
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File(s));
			} catch (IOException e) {
			}
			String name = new File(s).getName();
			try {
				BufferedImage imgResized = Scalr.resize(img,
						Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width,
						height, Scalr.OP_ANTIALIAS);

				img.flush();

				if (s.toLowerCase().endsWith(".jpeg")
						| s.toLowerCase().endsWith(".jpg")) {
					File out = new File(outputdir, name);
					ImageIO.write(imgResized, "jpeg", out);
				}

				if (s.toLowerCase().endsWith(".png")) {
					File out = new File(outputdir, name);
					ImageIO.write(imgResized, "png", out);
				}

			} catch (IOException e) {
			}
			setProgress(100 * i / numFiles);
			i++;
		}
	}

	@Override
	public Void doInBackground() {
		// Set<String> files = loadFiles(main.getTextField());
		// Set<String> files = main.getFiles();
		resizeFiles();
		// System.out.println("donzo");
		return null;
	}

	@Override
	protected void done() {
		System.out.println("WS!");
		super.done();
	}

}
