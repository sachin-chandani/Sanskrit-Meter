package mi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeterIdentification {
	public String gaRa_pat[] = { "lgg", "glg", "ggl", "gll", "lgl", "llg",
			"lll", "ggg" };
	public String gaRa_n[] = { "y", "r", "t", "B", "j", "s", "n", "m" };
	public String syllables[] = new String[500];
	public String longstring;
	public int num_metre = 1000;
	public String metre_name[] = new String[num_metre];
	public String metre_pat[] = new String[num_metre];
	public int numSylVerse = 0;
	public int metre_entered = 0;
	public int numPatterns = 0;
	public String pats[] = new String[num_metre];
	public String arDa_metre[] = new String[num_metre];
	public String arDa_pat[] = new String[num_metre];
	public int num_arDa = 0;
	public String vizama_metre[] = new String[num_metre];
	public String vizama_pat[] = new String[num_metre];
	public int num_vizama = 0;
	public String upajAti_metre[] = new String[num_metre];
	public String upajAti_pat[] = new String[num_metre];
	public int num_upajAti = 0;
	public int num_mAtrA_metre = 0;
	public String mAtrA_metre[] = new String[num_metre];
	public String mAtrA_pat[] = new String[num_metre];
	public int num_mAtrA_regex = 0;
	public String mAtrA_regex[] = new String[num_metre];
	public String mAtrA_pat_all[] = new String[num_metre];
	public String orig_input = "";
	public int radio_input = 3;
	public StringBuilder output = new StringBuilder();

	public String process(String verse, int radio) throws Exception {

		BufferedReader brm = new BufferedReader(new FileReader(
				"../webapps/mitweb/samavftta.txt"));
		String s = "";
		while ((s = brm.readLine()) != null) {
			String tmp[] = s.trim().split(" ");
			metre_name[metre_entered] = tmp[0];
			metre_pat[metre_entered] = tmp[1];
			if (tmp.length > 2) {
				if (tmp[2].equals("vA")) {
					pats[numPatterns] = tmp[1];// this takes vA;
					numPatterns++;
				} else {
					output.append("Error in input: " + s);
				}
			}
			metre_entered++;
		}
		brm.close();
		brm = new BufferedReader(new FileReader(
				"../webapps/mitweb/arDasama.txt"));
		while ((s = brm.readLine()) != null) {
			String tmp[] = s.trim().split(" ");
			arDa_metre[num_arDa] = tmp[0];
			arDa_pat[num_arDa] = "";
			for (int i = 1; i < tmp.length; i++) {
				if (tmp[i].equals("vA")) {
					pats[numPatterns] = tmp[i - 1];
					numPatterns++;
				} else {
					arDa_pat[num_arDa] += " " + tmp[i];
				}
			}
			arDa_pat[num_arDa] = arDa_pat[num_arDa].trim();
			num_arDa++;
		}
		brm.close();
		brm = new BufferedReader(new FileReader("../webapps/mitweb/vizama.txt"));
		while ((s = brm.readLine()) != null) {
			String tmp[] = s.trim().split(" ");
			vizama_metre[num_vizama] = tmp[0];
			vizama_pat[num_vizama] = "";
			for (int i = 1; i < tmp.length; i++) {
				if (tmp[i].equals("vA")) {
					pats[numPatterns] = tmp[i - 1];
					numPatterns++;
				} else {
					vizama_pat[num_vizama] += " " + tmp[i];
				}
			}
			vizama_pat[num_vizama] = vizama_pat[num_vizama].trim();
			num_vizama++;
		}
		brm.close();
		brm = new BufferedReader(
				new FileReader("../webapps/mitweb/upajAti.txt"));
		while ((s = brm.readLine()) != null) {
			String tmp[] = s.trim().split(" ");
			upajAti_metre[num_upajAti] = tmp[0];
			upajAti_pat[num_upajAti] = "";
			for (int i = 1; i < tmp.length; i++) {
				if (tmp[i].equals("vA")) {
					pats[numPatterns] = tmp[i - 1];
					numPatterns++;
				} else {
					upajAti_pat[num_upajAti] += " " + tmp[i];
				}
			}
			upajAti_pat[num_upajAti] = upajAti_pat[num_upajAti].trim();
			num_upajAti++;
		}
		brm.close();
		brm = new BufferedReader(new FileReader(
				"../webapps/mitweb/AryA_metre.txt"));
		while ((s = brm.readLine()) != null) {
			String tmp[] = s.trim().split(" ");
			mAtrA_metre[num_mAtrA_metre] = tmp[0];
			mAtrA_pat[num_mAtrA_metre] = "";
			for (int i = 1; i < tmp.length; i++) {
				if (!tmp[i].equals("vA")) {
					mAtrA_pat[num_mAtrA_metre] += " " + tmp[i];
					mAtrA_pat_all[num_mAtrA_regex] = tmp[i];
					boolean optional_vA = false;
					if (i < tmp.length - 1) {
						if (tmp[i + 1].equals("vA")) {
							optional_vA = true;
						}
					}
					mAtrA_regex[num_mAtrA_regex] = make_regex_mAtrA(tmp[i],
							optional_vA);
					// System.out.println( mAtrA_regex[num_mAtrA_regex]);
					num_mAtrA_regex++;
				}
			}
			mAtrA_pat[num_mAtrA_metre] = mAtrA_pat[num_mAtrA_metre].trim();
			num_mAtrA_metre++;
		}
		brm.close();
		String longString = verse;
		int vC = 0;
		// while((si=br.readLine())!=null){
		radio_input = radio;// output should depend on its input
		/*
		 * 1 a single pAda, 2. or two pAdas 3. or full verse 4. or more than one
		 * verse 5. or verse with extra text.
		 */
		numSylVerse = 0;
		vC++;
		// String longString = si.replace(" ", " ");
		output.append("<br><font color=#B74E11>" + longString + "</font>"
				+ "<br>");
		output.append("<br><b>Meter details:</b>");
		// br.close();
		// === preprocessing to remove spaces etc. ====//
		Pattern p = Pattern.compile("[^a-zA-Z\\.]+");
		Matcher m = p.matcher(longString);
		StringBuffer sb = new StringBuffer(500);
		while (m.find()) {
			m.appendReplacement(sb, Matcher.quoteReplacement(""));
		}
		m.appendTail(sb);

		longString = sb.toString();
		// === preprocessing to remove spaces etc. ====//
		String firstLine = "";
		String secondLine = "";
		boolean isFound = false;
		int indexDot = longString.indexOf(".");
		int indexDouble = longString.indexOf("..");
		String metre_here = "none_found";
		if ((indexDot > 0) && (indexDouble > indexDot) && (radio_input == 3)) {// full
			// verse
			// case
			firstLine = longString.substring(0, longString.indexOf("."));
			secondLine = longString.substring(longString.indexOf(".") + 1,
					longString.lastIndexOf(".."));
			try {
				metre_here = main_metre(firstLine, secondLine, output);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!(metre_here.equals("none_found"))) {
				isFound = true;
			}
			output.append(" " + metre_here);

		} else {// Assume that no dots are provided
			longString = longString.replace(".", "");
			if ((radio_input == 1) || (radio_input == 2)) {// only one pAda is
				// provided
				orig_input = longString;
				System.out.println("Radio input ..." + radio_input);
				firstLine = longString;
				secondLine = firstLine;
				try {
					metre_here = main_metre(firstLine, secondLine, output);
					System.out.println(firstLine + "\t" + secondLine + "\t"
							+ metre_here);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!(metre_here.equals("none_found"))) {
					isFound = true;
					output.append(" " + metre_here);
					// fr.write(metre_here);
				}
			} else if (radio_input == 3) {
				int numSyls = regexChecker(
						"[yvrlYmNRnJBGQDjbgqdKPCWTcwtkpSzsh]*[aAiIuUfFxXeEoO][HM]*",
						longString);
				int sylInds[] = sylInd(
						"[yvrlYmNRnJBGQDjbgqdKPCWTcwtkpSzsh]*[aAiIuUfFxXeEoO][HM]*",
						longString, numSyls);
				loopO: for (int ii = 0; ii < numSyls - 1; ii++) {
					numSylVerse = 0;
					firstLine = longString.substring(0, sylInds[ii + 1]);
					secondLine = longString.substring(sylInds[ii + 1]);
					try {
						metre_here = main_metre(firstLine, secondLine, output);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (!(metre_here.equals("none_found"))) {
						isFound = true;
						output.append(" " + metre_here);
						// fr.write(metre_here);
						break loopO;
					}
				}
			}
		}
		if (!isFound) {// check if some other words are present as well
			if (radio_input == 4 || radio_input == 5) {
				boolean toCont = true;
				while (toCont) {
					toCont = false;
					int numSyls = regexChecker(
							"[yvrlYmNRnJBGQDjbgqdKPCWTcwtkpSzsh]*[aAiIuUfFxXeEoO][HM]*",
							longString);
					// System.out.println(numSyls);
					if (numSyls >= 16) {
						int sylInds[] = sylInd(
								"[yvrlYmNRnJBGQDjbgqdKPCWTcwtkpSzsh]*[aAiIuUfFxXeEoO][HM]*",
								longString, numSyls);
						loopOO: for (int jj = 0; jj < numSyls - 1; jj++) {// starting
							for (int kk = jj + 1; kk < numSyls; kk++) {// end
								loopO: for (int ii = jj; ii < kk - 1; ii++) {
									if ((ii - jj >= 15) && (kk - ii >= 15)) {
										numSylVerse = 0;
										firstLine = longString.substring(
												sylInds[jj], sylInds[ii + 1]);
										secondLine = longString.substring(
												sylInds[ii + 1], sylInds[kk]);
										try {
											metre_here = main_metre(firstLine,
													secondLine, output);
										} catch (IOException e) {
										}
										if (!(metre_here.equals("none_found"))) {
											isFound = true;
											output.append(" " + metre_here
													+ "\t");
											// fr.write(metre_here + "\t");
											if (radio_input == 4) {
												toCont = true;
												longString = longString
														.substring(sylInds[kk]);
											} else {
												toCont = false;
											}
											// System.out.println(longString);
											// fr.write(longString+"\n");
											break loopOO;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// fr1.close();
		// fr.write("\n");
		// }
		// br.close();fr.close();
		return output.toString();
	}

	public String main_metre(String firstLine, String secondLine,
			StringBuilder output) throws IOException {

		int numSyllable1 = regexChecker(
				"[yvrlYmNRnJBGQDjbgqdKPCWTcwtkpSzsh]*[aAiIuUfFxXeEoO][HM]*",
				firstLine);
		int len_line1 = firstLine.length();
		String output_print = "";
		// output_print += ("Syllables in the first half..." + numSyllable1 +
		// "\n");
		if ((radio_input == 1) || (radio_input == 2)) {// only first line has to
			// be checked
			int chars = 0;
			int lenSyl[] = new int[numSylVerse];
			for (int i = 0; i < numSyllable1; i++) {
				lenSyl[i] = syllables[i].length();
				chars += lenSyl[i];
			}
			if (chars < len_line1) {
				syllables[numSyllable1 - 1] += firstLine.substring(chars);
				chars = len_line1;
				lenSyl[numSyllable1 - 1] += len_line1 - chars;
			}
			if (radio_input == 1) {// only one pAda
				int numSyllable = numSylVerse;
				String metre_pAda[] = find_metre_pAda(lenSyl, syllables,
						numSyllable, 0);
				if (metre_pAda[0].length() > 0) {

					output.append("\n"
							+ ("<br>The verse is in " + metre_pAda[0] + " meter which is a samavftta.\n"));
					output.append("\n"
							+ (output_print + "<br>The metrical patterns are...\n<br>"));
					for (int i = 0; i < 1; i++) {
						output.append("\n"
								+ ("<br><br><b>Pāda " + (i + 1) + "..........\n</b>"));
						output.append("\n"
								+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp"
										+ metre_pAda[2] + "\n"));
					}

					return metre_pAda[0];
				} else {// check for anuzwuB
					String pp1 = make_optional_regex("ytBrjm");
					String pp2 = make_optional_regex("ytBrnm");
					Pattern pat1 = Pattern.compile("^[lg]" + pp1 + pp2
							+ "[lg]$");
					Matcher ma = pat1.matcher(metre_pAda[3]);
					boolean isPres = true;
					if (!ma.find()) {
						output.append("\n"
								+ (metre_pAda[3] + "<br>..No meter found for this String. Please check the input for errors.\n"));
						isPres = false;
					}
					if (isPres) {
						output.append("\n"
								+ ("<br>The verse is in anuzwuB meter which is a samavftta.\n"));
						output.append("\n"
								+ (output_print + "<br>The metrical patterns are...\n\n"));
						for (int i = 0; i < 1; i++) {
							output.append("\n"
									+ ("<br><br><b>Pāda " + (i + 1) + "..........\n</b>"));
							output.append("\n"
									+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
											+ metre_pAda[2] + "\n"));
						}

						return "anuzwuB";
						// metre_found=true;
					}
				}
			} else {// two pAdas
				String metre_pAda[][] = new String[2][4];
				if (numSyllable1 % 2 == 0) {// even number of syllables
					int numSyllable = numSyllable1 / 2;
					for (int pAda = 0; pAda < 2; pAda++) {// 4 pAdas
						int start = numSyllable * pAda;
						metre_pAda[pAda] = find_metre_pAda(lenSyl, syllables,
								numSyllable, start);
						// System.out.println("pAda " + pAda + " " +
						// metre_pAda[pAda][2]+"\nmetre_name..."+metre_pAda[pAda][0]+"\n");
					}
				}
				boolean samavftta = true;
				for (int i = 0; i < 2; i++) {
					if (metre_pAda[i][0].length() == 0) {
						samavftta = false;
					}
				}
				if (samavftta) {
					// atleast some meter found for all pAdas
					String first_metre = metre_pAda[0][0];
					// System.out.println(first_metre+"..."+first_metre);
					for (int i = 1; i < 2; i++) {
						if (!(metre_pAda[i][0].equals(first_metre))) {
							samavftta = false;
						}
					}
					if (samavftta) {
						output.append("<br><b>Meter details:</b>");
						output.append("\n"
								+ ("<br>The verse is in " + first_metre + " meter which is a samavftta.\n"));
						output.append("\n"
								+ (output_print + "<br>The metrical patterns are...\n\n<br>"));
						for (int i = 0; i < 2; i++) {
							output.append("\n"
									+ ("<br><br><b>Pāda " + (i + 1) + "..........\n</b>"));
							output.append("\n"
									+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
											+ metre_pAda[i][2] + "\n"));
						}

						return first_metre;
						// metre_found=true;
					}
				}
				// check for anuztuB
				// Pattern
				// pat1=Pattern.compile("[yrtBjsnm][rs][lg][lg]");Pattern
				// pat2=Pattern.compile("[yrtBjsnm][rs]l[lg]");
				String pp1 = make_optional_regex("ytBrjm");// System.out.println(pp1+"....213");System.out.println(metre_pAda[0][3]);
				String pp2 = make_optional_regex("ytBrnm");// System.out.println(pp2+"....214");System.out.println(metre_pAda[0][3]);
				Pattern pat1 = Pattern.compile("^[lg]" + pp1 + pp2 + "[lg]$");
				Pattern pat2 = Pattern.compile("^[lg]" + pp1 + "lgl[lg]$");
				Matcher ma = pat1.matcher(metre_pAda[0][3]);
				boolean isPres = true;
				if (!ma.find()) {
					isPres = false;
				}// System.out.println("218");}
				ma = pat2.matcher(metre_pAda[1][3]);
				if (!ma.find()) {
					isPres = false;
				}// System.out.println(metre_pAda[1][3]+"222");}

				if (isPres) {
					output.append("\n"
							+ ("<br>The verse is in anuzwuB meter which is a samavftta.\n"));
					output.append("\n"
							+ (output_print + "<br>The metrical patterns are...\n\n<br>"));
					for (int i = 0; i < 2; i++) {
						output.append("\n"
								+ ("<br><br><b>Pāda " + (i + 1) + "..........\n</b>"));
						output.append("\n"
								+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
										+ metre_pAda[i][2] + "\n"));
					}

					return "anuzwuB";
					// metre_found=true;
				}
				loop_ar: for (int i = 1; i < numSyllable1; i++) {
					int syl_pAda1 = i;
					int syl_pAda2 = numSyllable1 - i;
					int start = 0;
					int numSyllable = 0;
					for (int pAda = 0; pAda < 2; pAda++) {// 4 pAdas
						if (pAda % 2 == 0) {
							numSyllable = syl_pAda1;
						} else {
							numSyllable = syl_pAda2;
						}
						metre_pAda[pAda] = find_metre_pAda(lenSyl, syllables,
								numSyllable, start);
						start += numSyllable;
					}
					for (int pAda = 0; pAda < 2; pAda++) {
						if (metre_pAda[pAda][3].equals("t")) {
							metre_pAda[pAda][1] += "."
									+ find_optional(metre_pAda[pAda][1]);
						}
					}
					String first_metre = metre_pAda[0][1];
					String second_metre = metre_pAda[1][1];
					// System.out.println(first_metre+" "+second_metre);
					if (equates(metre_pAda[2][1], first_metre)) {
						String metre_here[] = find_metre_arDa(first_metre + " "
								+ second_metre);
						if (metre_here[0].length() > 0) {
							output.append("\n"
									+ ("<br>The verse is in " + metre_here[1] + " meter which is a arDasamavftta.\n"));
							output.append("\n"
									+ (output_print + "<br>The metrical patterns are...\n\n"));
							for (int ii = 0; ii < 2; ii++) {
								output.append("\n"
										+ ("<br><br>......Pāda " + (ii + 1) + ":::::::::\n"));
								output.append("\n"
										+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
												+ metre_pAda[ii][2] + "\n"));
							}
							// System.out.println("The verse is in arDasamavftta metre pattern "
							// + metre_here[0]
							// +" with patterns "+metre_here[1]);

							return metre_here[1];
						}
					}
				}
			}
			return "none_found";
		}
		int numSyllable2 = regexChecker(
				"[yvrlYmNRnJBGQDjbgqdKPCWTcwtkpSzsh]*[aAiIuUfFxXeEoO][HM]*",
				secondLine);

		// output_print += ("Syllables in the second half..." + numSyllable2 +
		// "\n");
		int len_line2 = secondLine.length();
		int chars = 0;
		int lenSyl[] = new int[numSylVerse];
		for (int i = 0; i < numSyllable1; i++) {
			lenSyl[i] = syllables[i].length();
			chars += lenSyl[i];
		}
		if (chars < len_line1) {
			syllables[numSyllable1 - 1] += firstLine.substring(chars);
			chars = len_line1;
			lenSyl[numSyllable1 - 1] += len_line1 - chars;
		}
		for (int i = numSyllable1; i < numSylVerse; i++) {
			lenSyl[i] = syllables[i].length();
			chars += lenSyl[i];
		}
		if (chars < len_line1 + len_line2) {
			syllables[numSylVerse - 1] += secondLine.substring(chars
					- len_line1);
			chars = len_line1 + len_line2;
			lenSyl[numSylVerse - 1] += len_line1 + len_line2 - chars;
		}

		// for (int i = 0; i < numSylVerse; i++) {
		// output_print += (syllables[i] + "  ");
		// }
		// output_print += ("\n");
		// the first case: that every pAda has the same number of syllables
		String metre_pAda[][] = new String[4][4];// 4 different metres for each
		// pAda, 3 strings,
		// metre_name, gaRa_pattern,
		// the output and the
		// weights
		boolean metre_found = false;
		if (numSyllable1 == numSyllable2) {
			// Thus both the lines have the same number of syllables
			if (numSyllable1 % 2 == 0) {// even number of syllables
				int numSyllable = numSylVerse / 4;
				for (int pAda = 0; pAda < 4; pAda++) {// 4 pAdas
					int start = numSyllable * pAda;
					metre_pAda[pAda] = find_metre_pAda(lenSyl, syllables,
							numSyllable, start);
					// System.out.println("pAda " + pAda + " " +
					// metre_pAda[pAda][2]+"\nmetre_name..."+metre_pAda[pAda][0]+"\n");
				}
				boolean samavftta = true;
				for (int i = 0; i < 4; i++) {
					if (metre_pAda[i][0].length() == 0) {
						samavftta = false;
					}
				}
				if (samavftta) {
					// atleast some meter found for all pAdas
					String first_metre = metre_pAda[0][0];
					// System.out.println(first_metre+"..."+first_metre);
					for (int i = 1; i < 4; i++) {
						if (!(metre_pAda[i][0].equals(first_metre))) {
							samavftta = false;
						}
					}
					if (samavftta) {
						output.append("\n"
								+ ("<br>The verse is in " + first_metre + " meter which is a samavftta.\n"));
						output.append("\n"
								+ (output_print + "<br>The metrical patterns are...\n\n"));
						for (int i = 0; i < 4; i++) {
							output.append("\n"
									+ ("<br><br><b>Pāda " + (i + 1) + "..........\n</b></b>"));
							output.append("\n"
									+ ("<br>Syllables&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
											+ metre_pAda[i][2] + "\n"));
						}

						return first_metre;
						// metre_found=true;
					}
				}
				// check for anuztuB
				// Pattern
				// pat1=Pattern.compile("[yrtBjsnm][rs][lg][lg]");Pattern
				// pat2=Pattern.compile("[yrtBjsnm][rs]l[lg]");
				String pp1 = make_optional_regex("ytBrjm");// System.out.println(pp1+"....213");System.out.println(metre_pAda[0][3]);
				String pp2 = make_optional_regex("ytBrnm");// System.out.println(pp2+"....214");System.out.println(metre_pAda[0][3]);
				Pattern pat1 = Pattern.compile("^[lg]" + pp1 + pp2 + "[lg]$");
				Pattern pat2 = Pattern.compile("^[lg]" + pp1 + "lgl[lg]$");
				Matcher ma = pat1.matcher(metre_pAda[0][3]);
				boolean isPres = true;
				if (!ma.find()) {
					isPres = false;
				}// System.out.println("218");}
				ma = pat1.matcher(metre_pAda[2][3]);
				if (!ma.find()) {
					isPres = false;
				}// System.out.println("220");}
				ma = pat2.matcher(metre_pAda[1][3]);
				if (!ma.find()) {
					isPres = false;
				}// System.out.println(metre_pAda[1][3]+"222");}
				ma = pat2.matcher(metre_pAda[3][3]);
				if (!ma.find()) {
					isPres = false;
				}// System.out.println(metre_pAda[3][3]+"224");}
				if (isPres) {
					metre_found = true;
					output.append("\n"
							+ ("<br>The verse is in anuzwuB meter which is a samavftta.\n"));
					output.append("\n"
							+ (output_print + "<br>The metrical patterns are...\n\n"));
					for (int i = 0; i < 4; i++) {
						output.append("\n"
								+ ("<br><br><b>Pāda " + (i + 1) + "..........\n</b>"));
						output.append("\n"
								+ ("<br>Syllables&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
										+ metre_pAda[i][2] + "\n"));
					}

					return "anuzwuB";
					// metre_found=true;
				}
			}

			// general loop to change the number of syllables and search for the
			// pattern in files in arDasamavftta
			loop_ar: for (int i = 1; i < numSyllable1; i++) {
				int syl_pAda1 = i;
				int syl_pAda2 = numSyllable1 - i;
				int start = 0;
				int numSyllable = 0;
				for (int pAda = 0; pAda < 4; pAda++) {// 4 pAdas
					if (pAda % 2 == 0) {
						numSyllable = syl_pAda1;
					} else {
						numSyllable = syl_pAda2;
					}
					metre_pAda[pAda] = find_metre_pAda(lenSyl, syllables,
							numSyllable, start);
					start += numSyllable;
				}
				for (int pAda = 0; pAda < 4; pAda++) {
					if (metre_pAda[pAda][3].equals("t")) {
						metre_pAda[pAda][1] += "."
								+ find_optional(metre_pAda[pAda][1]);
					}
				}
				String first_metre = metre_pAda[0][1];
				String second_metre = metre_pAda[1][1];
				// System.out.println(first_metre+" "+second_metre);
				if (equates(metre_pAda[2][1], first_metre)) {
					if (equates(metre_pAda[3][1], second_metre)) {
						String metre_here[] = find_metre_arDa(first_metre + " "
								+ second_metre);
						if (metre_here[0].length() > 0) {
							output.append("\n"
									+ ("<br>The verse is in " + metre_here[1] + " meter which is a arDasamavftta.\n"));
							output.append("\n"
									+ (output_print + "<br>The metrical patterns are...\n\n"));
							for (int ii = 0; ii < 4; ii++) {
								output.append("\n"
										+ ("<br><br>......Pāda " + (ii + 1) + ":::::::::\n"));
								output.append("\n"
										+ ("<br>Syllables&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
												+ metre_pAda[ii][2] + "\n"));
							}
							// System.out.println("The verse is in arDasamavftta metre pattern "
							// + metre_here[0]
							// +" with patterns "+metre_here[1]);

							return metre_here[1];
							// metre_found=true;
							// break loop_ar;
						}
					}
				}
			}
		}
		// general loop to change the number of syllables and search for the
		// pattern in files in vizamavftta
		int syl_pAda[] = new int[4];
		loop_viz: for (int i = 1; i < numSyllable1; i++) {
			syl_pAda[0] = i;
			syl_pAda[1] = numSyllable1 - i;
			for (int j = 1; j < numSyllable2; j++) {
				syl_pAda[2] = j;
				syl_pAda[3] = numSyllable2 - j;
				int start = 0;
				int numSyllable = 0;
				for (int pAda = 0; pAda < 4; pAda++) {
					numSyllable = syl_pAda[pAda];
					metre_pAda[pAda] = find_metre_pAda(lenSyl, syllables,
							numSyllable, start);
					start += numSyllable;
				}
				for (int pAda = 0; pAda < 4; pAda++) {
					if (metre_pAda[pAda][3].equals("t")) {
						metre_pAda[pAda][1] += "|"
								+ find_optional(metre_pAda[pAda][1]);
					}
				}
				String pat_to_search = metre_pAda[0][1];
				String syl_pat = syl_pAda[0] + "";
				for (int pAda = 1; pAda < 4; pAda++) {
					pat_to_search += " " + metre_pAda[pAda][1];
					syl_pat += " " + syl_pAda[pAda];
				}
				// System.out.println(syl_pat+":::::"+pat_to_search);
				String metre_here[] = find_metre_vizama(pat_to_search);
				if (metre_here[0].length() > 0) {
					output.append("\n"
							+ ("<br>The verse is in " + metre_here[1] + " meter which is a vizamavftta.\n"));
					output.append("\n"
							+ (output_print + "<br>The metrical patterns are...\n\n"));
					for (int ii = 0; ii < 4; ii++) {
						output.append("\n"
								+ ("<br><br>......Pāda " + (ii + 1) + ":::::::::\n"));
						output.append("\n"
								+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp:&nbsp;&nbsp;"
										+ metre_pAda[ii][2] + "\n"));
					}

					return metre_here[1];
					// metre_found=true;
					// break loop_viz;
				}
			}
		}
		// general loop to change the number of syllables and search for the
		// pattern in files in upajAti
		// int syl_pAda[]=new int[4];
		loop_upaj: for (int i = 1; i < numSyllable1; i++) {
			syl_pAda[0] = i;
			syl_pAda[1] = numSyllable1 - i;
			for (int j = 1; j < numSyllable2; j++) {
				syl_pAda[2] = j;
				syl_pAda[3] = numSyllable2 - j;
				int start = 0;
				int numSyllable = 0;
				for (int pAda = 0; pAda < 4; pAda++) {
					numSyllable = syl_pAda[pAda];
					metre_pAda[pAda] = find_metre_pAda(lenSyl, syllables,
							numSyllable, start);
					start += numSyllable;
				}
				for (int pAda = 0; pAda < 4; pAda++) {
					if (metre_pAda[pAda][3].equals("t")) {
						metre_pAda[pAda][1] += "|"
								+ find_optional(metre_pAda[pAda][1]);
					}
				}
				String pat_to_search = metre_pAda[0][1];
				String syl_pat = syl_pAda[0] + "";
				for (int pAda = 1; pAda < 4; pAda++) {
					pat_to_search += " " + metre_pAda[pAda][1];
					syl_pat += " " + syl_pAda[pAda];
				}
				// System.out.println(syl_pat+":::::"+pat_to_search);
				String metre_here[] = find_metre_upajAti(pat_to_search);
				if (metre_here[0].length() > 0) {
					output.append("\n"
							+ ("<br>The verse is in " + metre_here[1] + " meter which is a upajAti.\n"));
					output.append("\n"
							+ (output_print + "<br>The metrical patterns are...\n\n"));
					for (int ii = 0; ii < 4; ii++) {
						output.append("\n"
								+ ("<br><br><b>Pāda " + (ii + 1) + "..........</b>\n"));
						output.append("\n"
								+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"
										+ metre_pAda[ii][2] + "\n"));
					}

					return metre_here[1];
					// metre_found=true;
					// break loop_upaj;
				}
			}
		}

		// Now using the mAtrAs to find the meter ..ArDas
		String pat_pAda[] = new String[2];
		pat_pAda[0] = find_mAtrA_ArDa(lenSyl, syllables, numSyllable1, 0);
		pat_pAda[1] = find_mAtrA_ArDa(lenSyl, syllables, numSyllable2,
				numSyllable1);
		// System.out.println(pat_pAda[0]+" - mAtrA - "+pat_pAda[1]);
		if ((pat_pAda[0].length() > 0) && (pat_pAda[1].length() > 0)) {
			// System.out.println(pat_pAda[0]+" - mAtrA - "+pat_pAda[1]);
			String metre_here[] = find_metre_mAtrA(pat_pAda[0] + " "
					+ pat_pAda[1]);
			if (metre_here[0].length() > 0) {
				metre_found = true;
				output.append("\n"
						+ (output_print + "<br>The verse is in "
								+ metre_here[1] + " meter which is a mAtrAvftta\n"));
				output.append("\n"
						+ ("<br>Syllables &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;"));
				for (int i = 0; i < numSylVerse; i++) {
					output.append("\n" + (syllables[i] + "  "));
				}
				output.append("\n" + ("\n"));
				return metre_here[1];
			}
		}
		return "none_found";
		// if(!metre_found){
		// System.out.println("no registered pattern found in the input verse. The database is being updated. Please check for any error in the input.");
		// fr.write("none_found\n");
		// }

	}

	public String find_optional(String gaRa_pattern) {
		return gaRa_pattern.substring(0, gaRa_pattern.length() - 1) + "l";
	}

	public boolean equates(String s1, String s2) {
		if (s1.equals(s2)) {
			return true;
		}
		String tmp1[] = s1.trim().split("[|]");
		String tmp2[] = s2.trim().split("[|]");
		for (int i = 0; i < tmp1.length; i++) {
			for (int j = 0; j < tmp2.length; j++) {
				if (tmp1[i].equals(tmp2[j])) {
					return true;
				}
			}
		}
		return false;
	}

	public String make_optional_regex(String pattern) {
		String out = "";
		for (int i = 0; i < pattern.length(); i++) {
			char search = pattern.charAt(i);
			switch (search) {
			case 'y':
				out += "lgg|";
				break;
			case 'r':
				out += "glg|";
				break;
			case 't':
				out += "ggl|";
				break;
			case 'B':
				out += "gll|";
				break;
			case 'j':
				out += "lgl|";
				break;
			case 's':
				out += "llg|";
				break;
			case 'n':
				out += "lll|";
				break;
			case 'm':
				out += "ggg|";
				break;
			default:
				break;
			}
		}
		out = out.substring(0, out.length() - 1);

		return "(" + out + ")";
	}

	public String make_regex_mAtrA(String pattern, boolean optional_vA) {
		String out = "";
		for (int i = 0; i < pattern.length(); i++) {
			char search = pattern.charAt(i);
			switch (search) {
			case 'G':
				out += "(gg)";
				break;
			case 'B':
				out += "(gll)";
				break;
			case 'j':
				out += "(lgl)";
				break;
			case 's':
				out += "(llg)";
				break;
			case 'L':
				out += "(llll)";
				break;
			case 'J':
				out += "(gg|gll|llg|llll)";
				break;
			case '4':
				out += "(gg|gll|llg|lgl|llll)";
				break;
			default:
				out += "(" + search + ")";
				break;
			}
		}
		if (optional_vA) {
			if (out.substring(out.length() - 3).equals("(g)")) {
				out = out.substring(0, out.length() - 3) + "(l|g)";
			}
		}
		return out;
	}

	public String[] find_metre_pAda(int lenSyl[], String syllables[],
			int numSyllable, int start) {
		String metre_pAda = "";
		String gaRa_pattern = "";
		String output_print = "";
		String vA_applied = "f";
		String out_weights = "";
		boolean weight[] = new boolean[numSyllable];
		for (int i = 0; i < numSyllable; i++) {
			String syl = syllables[start + i];
			output_print += (syl + " ");
			int vowel_ind = find_vowel_ind(syl);
			int cons_here = syl.length() - 1 - vowel_ind;
			char vowel = syl.charAt(vowel_ind);
			char last_char = syl.charAt(syl.length() - 1);
			String long_vowels = "AIUeEoOFX";
			if (long_vowels.indexOf(vowel) != -1) {
				weight[i] = false;
			} else {
				if (i == numSyllable - 1) {
					String guru_cons = "MHm";
					if (guru_cons.indexOf(last_char) != -1) {
						weight[i] = false;
					} else {
						weight[i] = true;
					}
				} else if (start + i + 1 < numSylVerse) {
					int vowel_ind_next = find_vowel_ind(syllables[start + i + 1])
							+ cons_here;
					if (vowel_ind_next >= 2) {
						weight[i] = false;
					}// conjunct consonant
					else {
						weight[i] = true;
					}
				}
			}
		}
		int numGroups = numSyllable / 3;
		if (numSyllable % 3 != 0) {
			numGroups++;
		}
		String sylGroup[][] = new String[numGroups][3];
		boolean wtGroup[][] = new boolean[numGroups][3];
		for (int i = 0; i < numGroups; i++) {
			sylGroup[i][0] = syllables[start + 3 * i];
			wtGroup[i][0] = weight[3 * i];
			if (3 * i + 1 < numSyllable) {
				sylGroup[i][1] = syllables[start + 3 * i + 1];
				wtGroup[i][1] = weight[3 * i + 1];
			}
			if (3 * i + 2 < numSyllable) {
				sylGroup[i][2] = syllables[start + 3 * i + 2];
				wtGroup[i][2] = weight[3 * i + 2];
			}
		}

		String gaRa[] = new String[numGroups];
		boolean isFull = false;
		if (numSyllable % 3 == 0) {
			isFull = true;
		}
		if (!isFull) {
			for (int i = 0; i < numGroups - 1; i++) {
				gaRa[i] = gaRa_name(wtGroup[i]);
				// System.out.print(gaRa[i]+"  ");
			}
			gaRa[numGroups - 1] = out_weight(wtGroup[numGroups - 1][0]);
			if (numSyllable % 3 > 1)
				gaRa[numGroups - 1] += out_weight(wtGroup[numGroups - 1][1]);
		} else {
			for (int i = 0; i < numGroups; i++) {
				gaRa[i] = gaRa_name(wtGroup[i]);
				// System.out.print(gaRa[i]+" ");
			}
		}
		String metre_type = find_metre(gaRa);
		metre_pAda = metre_type;
		String backup_metre = metre_type;
		// metre_pAda[pAda]=
		String backup = "";
		// if(metre_type.length()>0){

		backup += ("\n");
		backup += ("<br>Weights &nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;:&nbsp;&nbsp;");
		String color = new String();
		for (int i = 0; i < numSyllable; i++) {
			color += (out_weight(weight[i]) + "  ");

		}
		color = color.replaceAll("g", "G");
		color = color.replaceAll("l", "<font color=#053BFC>L</font>");
		backup += color;

		// for (int i = 0; i < numSyllable; i++) {
		// backup += (out_weight(weight[i]) + "  ");
		// out_weights += out_weight(weight[i]);

		// }

		backup += ("<br>Ganas &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;");
		for (int i = 0; i < numGroups - 1; i++) {
			backup += (gaRa[i] + " ");
			gaRa_pattern += gaRa[i];
		}

		if (!isFull) {
			for (int i = 0; i < 2; i++) {
				if (sylGroup[numGroups - 1][i] != null) {
					backup += (out_weight(wtGroup[numGroups - 1][i]) + " ");
					gaRa_pattern += out_weight(wtGroup[numGroups - 1][i]);
				}
			}
		} else {
			backup += (gaRa[numGroups - 1] + " ");
			gaRa_pattern += gaRa[numGroups - 1];
		}
		backup += "\n";
		// backup +=("Meter Name        :\t"+metre_type+"\n");
		backup += ("<br>Syllable count :&nbsp;&nbsp;" + numSyllable);
		// System.out.println(backup);
		// backup
		// +=("\t\t\ty=yagaRaH,\tr=ragaRaH,\tt=tagaRaH,\n\t\t\tB=BagaRaH,\tj=jagaRaH,\ts=sagaRaH,\n\t\t\tm=magaRaH,"
		// + "\tn=nagaRaH,\n\t\t\tl=laGuH,\tg=guruH");
		// }
		boolean vA_padA_applied = false;

		// Applying vA padAnte

		if (weight[numSyllable - 1]) {
			weight[numSyllable - 1] = false;
			for (int i = numGroups - 1; i < numGroups; i++) {
				// System.out.println(i+"..."+numSyllable);
				if (3 * i + 3 == numSyllable) {
					wtGroup[i][2] = weight[3 * i + 2];
					gaRa[i] = gaRa_name(wtGroup[i]);
					// System.out.println(gaRa[i]);
				}
				// sylGroup[i][0]=syllables[3*i];
				// wtGroup[i][0]=weight[3*i];
				else {
					if (3 * i + 1 <= numSyllable) {
						sylGroup[i][0] = syllables[3 * i];
						wtGroup[i][0] = weight[3 * i];
						if (wtGroup[i][0])
							gaRa[numGroups - 1] = "l";
						else
							gaRa[numGroups - 1] = "g";
					}
					if (3 * i + 2 <= numSyllable) {
						sylGroup[i][1] = syllables[3 * i + 1];
						wtGroup[i][1] = weight[3 * i + 1];
						if (wtGroup[i][1])
							gaRa[numGroups - 1] += "l";
						else
							gaRa[numGroups - 1] += "g";
					}
				}
			}

			String pos_g_pat = "";
			for (int i = 0; i < numGroups; i++) {
				pos_g_pat += gaRa[i];
			}
			// gaRa_pattern.substring(0,gaRa_pattern.length()-1)+gaRa[numGroups-1];//we
			// will check vA condition on this pattern
			metre_type = find_metre(gaRa);
			// if(metre_type.length()>0){
			boolean is_optional = find_vA(pos_g_pat);
			// System.out.println(pos_g_pat+"..."+is_optional);
			if (is_optional) {
				gaRa_pattern = "";
				vA_applied = "t";
				metre_pAda = metre_type;
				vA_padA_applied = true;
				output_print += ("\n");
				String color2 = "";
				output_print += ("<br>Weights &nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;:&nbsp;&nbsp;<br>");
				for (int i = 0; i < numSyllable; i++) {
					color2 += (out_weight(weight[i]) + "  ");
				}
				color2 = color2.replaceAll("g", "G");
				color2 = color2.replaceAll("l", "<font color=#053BFC>L</font>");
				output_print += color2;
				output_print += ("<br>Ganas &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;");
				for (int i = 0; i < numGroups - 1; i++) {
					output_print += (gaRa[i] + " ");
					gaRa_pattern += gaRa[i];
				}
				if (!isFull) {
					for (int i = 0; i < 2; i++) {
						if (sylGroup[numGroups - 1][i] != null) {
							output_print += (out_weight(wtGroup[numGroups - 1][i]) + " ");
							gaRa_pattern += out_weight(wtGroup[numGroups - 1][i]);
						}
					}
				} else {
					output_print += (gaRa[numGroups - 1] + " ");
					gaRa_pattern += gaRa[numGroups - 1];
				}
				output_print += "\n";
				// output_print+=("Meter Name        :\t"+metre_type+"\n");
				output_print += ("<br>Syllable count :&nbsp;&nbsp;" + numSyllable);
				// output_print+=("\t\t\ty=yagaRaH,\tr=ragaRaH,\tt=tagaRaH,\n\t\t\tB=BagaRaH,\tj=jagaRaH,\ts=sagaRaH,\n\t\t\tm=magaRaH,"
				// + "\tn=nagaRaH,\n"+ "\t\t\tl=laGuH,\tg=guruH\n");
			}

		}
		if (!vA_padA_applied) {
			output_print += (backup);
		}
		output_print += "\n";
		String output[] = { metre_pAda, gaRa_pattern, output_print,
				out_weights, vA_applied };
		return output;
	}

	public String find_mAtrA_ArDa(int lenSyl[], String syllables[],
			int numSyllable, int start) {
		String metre_pAda = "";
		String gaRa_pattern = "";
		String output_print = "";
		String vA_applied = "f";
		boolean weight[] = new boolean[numSyllable];
		for (int i = 0; i < numSyllable; i++) {
			String syl = syllables[start + i];
			output_print += (syl + " ");
			int vowel_ind = find_vowel_ind(syl);
			int cons_here = syl.length() - 1 - vowel_ind;
			char vowel = syl.charAt(vowel_ind);
			char last_char = syl.charAt(syl.length() - 1);
			String long_vowels = "AIUeEoOFX";
			if (long_vowels.indexOf(vowel) != -1) {
				weight[i] = false;
			} else {
				if (i == numSyllable - 1) {
					String guru_cons = "MHm";
					if (guru_cons.indexOf(last_char) != -1) {
						weight[i] = false;
					} else {
						weight[i] = true;
					}
				} else if (start + i + 1 < numSylVerse) {
					int vowel_ind_next = find_vowel_ind(syllables[start + i + 1])
							+ cons_here;
					if (vowel_ind_next >= 2) {
						weight[i] = false;
					}// conjunct consonant
					else {
						weight[i] = true;
					}
				}
			}
		}
		String this_line = "";
		for (int i = 0; i < numSyllable; i++) {
			this_line = this_line + out_weight(weight[i]);
		}
		String output = "";
		for (int i = 0; i < num_mAtrA_regex; i++) {
			Pattern mAtrA_pat = Pattern.compile(mAtrA_regex[i]);
			Matcher m = mAtrA_pat.matcher(this_line);
			// System.out.println(this_line);
			if (m.find()) {
				// int all_groups=m.groupCount();
				// String
				output = output + " " + mAtrA_pat_all[i];
			}
		}
		return output.trim().replace(" ", "|");
	}

	public boolean find_vA(String gaRa_pattern) {
		for (int i = 0; i < numPatterns; i++) {
			if (pats[i].equals(gaRa_pattern)) {
				return true;
			}
		}
		return false;
	}

	public String find_metre(String[] gaRa) {
		String pat = "";
		for (int i = 0; i < gaRa.length; i++) {
			if (i == 0) {
				pat = gaRa[i];
			} else {
				pat = pat + "" + gaRa[i];
			}
		}
		String output = "";
		loop: for (int i = 0; i < metre_entered; i++) {
			if (pat.equals(metre_pat[i])) {
				output = metre_name[i];
				break loop;
			} else {
				output = "";
			}

		}
		return output;
	}

	public int find_vowel_ind(String syl) {
		String all_vowels = "aAiIuUfFxXeEoO";
		for (int i = 0; i < syl.length(); i++) {
			if (all_vowels.indexOf(syl.charAt(i)) != -1) {
				return i;
			}
		}
		output.append("<br>Error...no vowel in this syllable " + syl);
		return -1;
	}

	public String[] find_metre_arDa(String pattern) {
		String tmp[] = pattern.split(" ");
		String tmp1[][] = new String[tmp.length][2];
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].indexOf("|") != -1) {
				tmp1[i] = tmp[i].split("[|]");
			} else {
				tmp1[i][0] = tmp[i];
			}
		}
		String pat = "";
		for (int i = 0; i < 2; i++) {
			if (tmp1[0][i] != null) {
				pat = tmp1[0][i];
				for (int j = 0; j < 2; j++) {
					if (tmp1[1][j] != null) {
						pat = pat + " " + tmp1[1][j];
						for (int it = 0; it < num_arDa; it++) {
							if (arDa_pat[it].equals(pat)) {
								String output[] = { pat, arDa_metre[it] };
								return output;
							}
						}
					}
				}
			}
		}
		String output1[] = { "", "" };
		return output1;
	}

	public String[] find_metre_mAtrA(String pattern) {
		output.append("<br>Pattern is " + pattern);
		String tmp[] = pattern.split(" ");
		String tmp1[][] = new String[tmp.length][2];
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].indexOf("|") != -1) {
				tmp1[i] = tmp[i].split("[|]");
			} else {
				tmp1[i][0] = tmp[i];
			}
		}
		String pat = "";
		for (int i = 0; i < 2; i++) {
			if (tmp1[0][i] != null) {
				pat = tmp1[0][i];
				for (int j = 0; j < 2; j++) {
					if (tmp1[1][j] != null) {
						pat = pat + " " + tmp1[1][j];
						// System.out.println("loop:::"+pat);
						for (int it = 0; it < num_mAtrA_metre; it++) {
							if (mAtrA_pat[it].equals(pat)) {
								String output[] = { pat, mAtrA_metre[it] };
								return output;
							}
						}
					}
				}
			}
		}
		String output1[] = { "", "" };
		return output1;
	}

	public String[] find_metre_vizama(String pattern) {
		String tmp[] = pattern.split(" ");

		String tmp1[][] = new String[tmp.length][2];
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].indexOf("|") != -1) {
				tmp1[i] = tmp[i].split("[|]");
			} else {
				tmp1[i][0] = tmp[i];
			}
		}
		String pat = "";
		for (int i = 0; i < 2; i++) {
			if (tmp1[0][i] != null) {
				pat = tmp1[0][i];
				for (int j = 0; j < 2; j++) {
					if (tmp1[1][j] != null) {
						pat = pat + " " + tmp1[1][j];
						for (int k = 0; k < 2; k++) {
							if (tmp1[2][k] != null) {
								pat = pat + " " + tmp1[2][k];
								for (int l = 0; l < 2; l++) {
									if (tmp1[3][l] != null) {
										pat = pat + " " + tmp1[3][l];
										for (int it = 0; it < num_vizama; it++) {
											if (vizama_pat[it].equals(pat)) {
												String output[] = { pat,
														vizama_metre[it] };
												return output;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		String output1[] = { "", "" };
		return output1;
	}

	public String[] find_metre_upajAti(String pattern) {
		String tmp[] = pattern.split(" ");

		String tmp1[][] = new String[tmp.length][2];
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].indexOf("|") != -1) {
				tmp1[i] = tmp[i].split("[|]");
			} else {
				tmp1[i][0] = tmp[i];
			}
		}
		String pat = "";
		for (int i = 0; i < 2; i++) {
			if (tmp1[0][i] != null) {
				pat = tmp1[0][i];
				for (int j = 0; j < 2; j++) {
					if (tmp1[1][j] != null) {
						pat = pat + " " + tmp1[1][j];
						for (int k = 0; k < 2; k++) {
							if (tmp1[2][k] != null) {
								pat = pat + " " + tmp1[2][k];
								for (int l = 0; l < 2; l++) {
									if (tmp1[3][l] != null) {
										pat = pat + " " + tmp1[3][l];
										for (int it = 0; it < num_upajAti; it++) {
											if (upajAti_pat[it].equals(pat)) {
												String output[] = { pat,
														upajAti_metre[it] };
												return output;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		String output1[] = { "", "" };
		return output1;
	}

	public String out_weight(boolean weight) {
		if (weight) {
			return "l";
		} else {
			return "g";
		}

	}

	public String gaRa_name(boolean[] weight) {
		String pat = "";
		for (int i = 0; i < weight.length; i++) {
			pat = pat + out_weight(weight[i]);
		}
		String output = "";
		loop: for (int i = 0; i < 8; i++) {
			if (pat.equals(gaRa_pat[i])) {
				output = gaRa_n[i];
				break loop;
			}
		}
		if (output.length() == 0) {
			System.out.println(" <br>:For this pattern no metre is found"); // ("For the pattern "+pat+" nothing found");
		}
		return output;
	}

	public int regexChecker(String theRegex, String str2Check) {
		int numSyl = 0;
		// You define your regular expression (REGEX) using Pattern

		Pattern checkRegex = Pattern.compile(theRegex);

		// Creates a Matcher object that searches the String for
		// anything that matches the REGEX

		Matcher regexMatcher = checkRegex.matcher(str2Check);

		// Cycle through the positive matches and print them to screen
		// Make sure string isn't empty and trim off any whitespace
		// Please note that we need numSyl for one line and numSyllable for the
		// complete verse
		while (regexMatcher.find()) {
			if (regexMatcher.group().length() != 0) {
				syllables[numSylVerse] = regexMatcher.group().trim();
				// System.out.print( regexMatcher.group().trim()+" " );
				// System.out.println(numSyl+" "+syllables[numSylVerse]);
				numSyl++;
				numSylVerse++;
				// You can get the starting and ending indexs

			}
		}

		// System.out.println();
		return numSyl;
	}

	public int[] sylInd(String theRegex, String str2Check, int numSyls) {
		int indices[] = new int[numSyls];
		int numSyl = 0;
		// You define your regular expression (REGEX) using Pattern
		int nextInd = 0;
		Pattern checkRegex = Pattern.compile(theRegex);

		// Creates a Matcher object that searches the String for
		// anything that matches the REGEX

		Matcher regexMatcher = checkRegex.matcher(str2Check);

		// Cycle through the positive matches and print them to screen
		// Make sure string isn't empty and trim off any whitespace
		// Please note that we need numSyl for one line and numSyllable for the
		// complete verse
		while (regexMatcher.find()) {
			int thisLen = regexMatcher.group().length();
			if (thisLen != 0) {
				indices[numSyl] = nextInd;
				nextInd += thisLen;
				syllables[numSylVerse] = regexMatcher.group().trim();
				// System.out.print( regexMatcher.group().trim()+" " );
				// System.out.println(numSyl+" "+syllables[numSylVerse]);
				numSyl++;
				numSylVerse++;
				// You can get the starting and ending indexs

			}
		}

		// System.out.println();
		return indices;
	}

	public void regexReplace(String str2Replace) {

		// REGEX that matches 1 or more white space

		Pattern replace = Pattern.compile("\\s+"); // any single and more
		// whitespaces

		// This doesn't really apply, but this is how you ignore case
		// Pattern replace = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);

		// trim the string t prepare it for a replace

		Matcher regexMatcher = replace.matcher(str2Replace.trim());

		// replaceAll replaces all white space with commas

		// System.out.println(regexMatcher.replaceAll(", "));

	}
}