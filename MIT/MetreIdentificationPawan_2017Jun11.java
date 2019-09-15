import java.util.regex.*;
import java.io.*;
public class MetreIdentification {
    public static String gaRa_pat[]={"lgg","glg","ggl","gll","lgl","llg","lll","ggg"};
    public static String gaRa_n[]={"y","r","t","B","j","s","n","m"};
    /**
     *
     */
    public static String syllables[]=new String[500];
    public static String longstring;
    public static int num_meter=1000;
    public static String meter_name[]=new String[num_meter];
    public static String meter_pat[]=new String[num_meter];
    public static boolean vA_padA[]=new boolean[num_meter];
    public static int meter_entered=0;
	
	public static void main(String[] args) throws Exception{
            BufferedReader brm = new BufferedReader(new FileReader("newmetre.txt"));
            String s="";
            
            while((s=brm.readLine())!=null){
                String tmp[]=s.trim().split(" ");
                meter_name[meter_entered]=tmp[0];
                meter_pat[meter_entered]=tmp[1];
                if(tmp.length>2){
                    if(tmp[2].equals("vA")){
                        vA_padA[meter_entered]=true;
                    }
                    else{
                        System.out.println("Error in input: "+s);
                    }
                }
                meter_entered++;
            }
            brm.close();
            
            FileReader fr=new FileReader ("data.txt");
		BufferedReader br = new BufferedReader(fr);
                
                
                //String s="";
                //while((s=br.re))
                
		String longString = br.readLine();
                br.close();
		//=== preprocessing to remove spaces etc. ====//
                
                Pattern p = Pattern.compile("[^a-zA-Z]+");
                Matcher m = p.matcher(longString);
                StringBuffer sb = new StringBuffer(500);
                while(m.find()){
                    m.appendReplacement(sb, Matcher.quoteReplacement(""));
                    
                }
		m.appendTail(sb);
                // System.out.println(sb.toString());
                longString=sb.toString();
                //=== preprocessing to remove spaces etc. ====//
                
                
		int numSyllable = regexChecker("[yvrlYmNRnJBGQDjbgqdKPCWTcwtkpSzshHM]*[aAiIuUfFxXeEoO]", longString);
                boolean weight[]=new boolean[numSyllable];
                int lenSyl[]=new int[numSyllable];
                int chars=0;
                for(int i=0;i<numSyllable;i++){
                    lenSyl[i]=syllables[i].length();
                    chars+=lenSyl[i];
                }
                int orig_chars=longString.length();
                String extra="";
                if(orig_chars>chars){
                    extra=longString.substring(chars);
                    syllables[numSyllable-1]=syllables[numSyllable-1]+extra;
                }
                for(int i=0;i<numSyllable;i++){
                    String syl = syllables[i];
                    char vowel = syl.charAt(syl.length()-1);
                    String long_vowels="AIUeEoOFX";
                    if(long_vowels.indexOf(vowel)!=-1){
                        weight[i]=false;
                    }
                    else{
                        if(i==numSyllable-1){
                            String guru_cons="MHm";
                            if(guru_cons.indexOf(vowel)!=-1){
                                weight[i]=false;
                            }
                            else{
                                if(extra.length()==2){
                                    weight[i]=false;
                                }
                                else if(extra.length()==1){
                                    char vowel_prev = syllables[i].charAt(syllables[i].length()-2);
                                    if(long_vowels.indexOf(vowel_prev)!=-1){
                                        weight[i]=false;
                                    }
                                    else{
                                        weight[i]=true;
                                    }
                                }
                                else{
                                    weight[i]=true;
                                }
                            }
                        }
                        else{
                            int nxtLen=lenSyl[i+1];
                            if(nxtLen>2){
                                weight[i]=false;
                            }
                            else{
                                weight[i]=true;
                            }
                        }
                    }
                }
                
                int numGroups=numSyllable/3;
                if(numSyllable%3!=0){
                    numGroups++;
                }
                
                String sylGroup[][]=new String[numGroups][3];
                boolean wtGroup[][]=new boolean[numGroups][3];
                for(int i=0;i<numGroups;i++){
                    sylGroup[i][0]=syllables[3*i];
                    wtGroup[i][0]=weight[3*i];
                    if(3*i+1<numSyllable){
                        sylGroup[i][1]=syllables[3*i+1];
                        wtGroup[i][1]=weight[3*i+1];
                    }
                    if(3*i+2<numSyllable){
                        sylGroup[i][2]=syllables[3*i+2];
                        wtGroup[i][2]=weight[3*i+2];
                    }
                }
                System.out.print("Parsed string     :\t");
                String gaRa[]=new String[numGroups];
                for(int i=0;i<numSyllable;i++){
                    System.out.print(syllables[i]+"  ");
                }
                System.out.println();
               
                
                boolean isFull=false;
                if(numSyllable%3==0){isFull=true;}
                if(!isFull){
                    for(int i=0;i<numGroups-1;i++){
                        gaRa[i]=gaRa_name(wtGroup[i]);
                        //System.out.print(gaRa[i]+"  ");
                    }
                    gaRa[numGroups-1]=out_weight(wtGroup[numGroups-1][0]);
                    if (numSyllable % 3 > 1)
                        gaRa[numGroups-1] += out_weight(wtGroup[numGroups-1][1]);
                }
                else{
                    for(int i=0;i<numGroups;i++){
                        gaRa[i]=gaRa_name(wtGroup[i]);
                        //System.out.print(gaRa[i]+" ");
                    }
                }
               
               String meter_type=find_meter(gaRa);
               
               String backup="";
               if(meter_type.length()>0){
                backup += ("Syllable weights  :\t");
                for(int i=0;i<numSyllable;i++){
                    backup += (out_weight(weight[i])+"  ");
                    
                }
                backup += "\n";
                backup +=("Gana Pattern      :\t");
                for(int i=0;i<numGroups-1;i++){
                    backup +=(gaRa[i]+" ");
                }
                
                if(!isFull){
                    for(int i=0;i<2;i++){
                        if(sylGroup[numGroups-1][i]!=null){
                            backup +=(out_weight(wtGroup[numGroups-1][i])+" ");
                        }
                    }
                }
                else{
                    backup +=(gaRa[numGroups-1]+" ");
                }
                backup +="\n";
                
                backup +=("Meter Name        :\t"+meter_type+"\n");
                backup +=("No. of Syllables  :\t"+numSyllable+"\n");
                backup +=("\n");
                backup +=("\t\t\ty=yagaRaH,\tr=ragaRaH,\tt=tagaRaH,\n\t\t\tB=BagaRaH,\tj=jagaRaH,\ts=sagaRaH,\n\t\t\tm=magaRaH,"
                        + "\tn=nagaRaH,\n"
                        + "\t\t\tl=laGuH,\tg=guruH");
               }
               boolean vA_padA_applied=false;
             
               //Applying vA padAnte
               
               if(weight[numSyllable-1]){
                   weight[numSyllable-1]=false;
                    for(int i=numGroups-1;i<numGroups;i++){
                        //sylGroup[i][0]=syllables[3*i];
                        //wtGroup[i][0]=weight[3*i];
                        
                        if(3*i+1 <= numSyllable){
                            sylGroup[i][0] = syllables[3*i];
                            wtGroup[i][0]  = weight[3*i];
                            
                            if ( wtGroup[i][0]) 
                                gaRa[numGroups -1] = "l";
                            else gaRa[numGroups-1] = "g";
                        }
                        
                        if(3*i+2 <= numSyllable){
                            sylGroup[i][1] = syllables[3*i+1];
                            wtGroup[i][1]  = weight[3*i+1];
                              if ( wtGroup[i][1]) 
                                gaRa[numGroups -1] += "l";
                            else gaRa[numGroups-1] += "g";
                            
                            
                        }
                    }
                    
                    if(isFull){
                        gaRa[numGroups-1]=gaRa_name(wtGroup[numGroups-1]);
                    }

                   meter_type=find_meter(gaRa);
                   if(meter_type.length()>0){
                       boolean is_optional = find_vA(meter_type);
                       if(is_optional){
                           vA_padA_applied=true;
                           System.out.print("Syllable weights  :\t");
                            for(int i=0;i<numSyllable;i++){
                                System.out.print(out_weight(weight[i])+"  ");
                            }
                            System.out.println();
                            System.out.print("Gana Pattern      :\t");
                            for(int i=0;i<numGroups-1;i++){
                                System.out.print(gaRa[i]+" ");
                            }
                            if(!isFull){
                                for(int i=0;i<2;i++){
                                    if(sylGroup[numGroups-1][i]!=null){
                                        System.out.print(out_weight(wtGroup[numGroups-1][i])+" ");
                                    }
                                }
                            }
                            else{
                                System.out.print(gaRa[numGroups-1]+" ");
                            }
                            System.out.println();
                            System.out.println("Meter Name        :\t"+meter_type);
                            System.out.println("No. of Syllables  :\t"+numSyllable);
                            System.out.print("\n");
                            System.out.println("\t\t\ty=yagaRaH,\tr=ragaRaH,\tt=tagaRaH,\n\t\t\tB=BagaRaH,\tj=jagaRaH,\ts=sagaRaH,\n\t\t\tm=magaRaH,"
                        + "\tn=nagaRaH,\n"
                        + "\t\t\tl=laGuH,\tg=guruH");
                       }
                   }
                   
               }
               if(!vA_padA_applied){
                   System.out.println(backup);
               }
                    //print the weights
                    
                    //gaRa[i]=find_gaRa(wtGroup[i]);
                //}
                //Printing the meter type
              
              
               //System.out.println("l = laGu,    g = guru");
               System.out.println();
               /*if(numSyllable==8){
                    System.out.println("The meter type is      : anuzwup"); System.out.println();
                    System.out.println("Number of syllables is : " + numSyllable);System.out.println();
                }
                else if(numSyllable==11){
                    System.out.println("The meter type is      : trizwup");System.out.println();
                    System.out.println("Number of syllables is : " + numSyllable);System.out.println();
                }
                 else if(numSyllable==14){
                    System.out.println("The meter type is      : vasantatilakA");System.out.println();
                    System.out.println("Number of syllables is : " + numSyllable);System.out.println();
                 }
                else if(numSyllable==19){
                    System.out.println("The meter type is      : SArdUlavikrIDitam");System.out.println();
                    System.out.println("Number of syllables is : " + numSyllable);System.out.println();
                }
                 else {
                    System.out.println("Meter type not recognized yet");System.out.println();
                    System.out.println("Number of syllables is : " + numSyllable);System.out.println();
                }
		*/
	
			regexReplace(longString);
		
	}
        public static boolean find_vA(String meter_type){
            for(int i=0;i<meter_entered;i++){
                if(meter_name[i].equals(meter_type)){
                    if(vA_padA[i]){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
            return false;
        }
        public static String find_meter(String[] gaRa){
            String pat="";
            for(int i=0;i<gaRa.length;i++){
                if(i==0){
                    pat=gaRa[i];
                }
                else{
                    pat=pat+""+gaRa[i];
                }
            }
            String output="";
            loop:for(int i=0;i<meter_entered;i++){
                if(pat.equals(meter_pat[i])){
                    output=meter_name[i];
                    break loop;
                }
                else{
                    output="No metre exists for this pattern!!";
                }
                    
            }
            
           // if(output.length()==0){
             //   System.out.println(" : For this pattern no meter is found"); //("For the pattern "+pat+" nothing found");
           // }
            return output;
        }
	public static String out_weight(boolean weight){
            if(weight){return "l";}
            else{return "g";}
                    
        }
        public static String gaRa_name(boolean[] weight){
            String pat="";
            for(int i=0;i<weight.length;i++){
                pat=pat+out_weight(weight[i]);
            }
            String output="";
            loop:for(int i=0;i<8;i++){
                if(pat.equals(gaRa_pat[i])){
                    output=gaRa_n[i];break loop;
                }
            }
            if(output.length()==0){
                System.out.println(" :For this pattern no meter is found"); //("For the pattern "+pat+" nothing found");
            }
            return output;
        }
	public static int regexChecker(String theRegex, String str2Check){
		int numSyl=0;
		// You define your regular expression (REGEX) using Pattern
		
		Pattern checkRegex = Pattern.compile(theRegex);
		
		// Creates a Matcher object that searches the String for
		// anything that matches the REGEX
		
		Matcher regexMatcher = checkRegex.matcher( str2Check );
		
		// Cycle through the positive matches and print them to screen
		// Make sure string isn't empty and trim off any whitespace
		
		while ( regexMatcher.find() ){
			if (regexMatcher.group().length() != 0){
                            syllables[numSyl]=regexMatcher.group().trim();
				//System.out.print( regexMatcher.group().trim()+" " );
				numSyl++;
				// You can get the starting and ending indexs
				
				}
		}
		
		System.out.println();
                return numSyl;
	}
	
	public static void regexReplace(String str2Replace){
		
		// REGEX that matches 1 or more white space
		
		Pattern replace = Pattern.compile("\\s+"); //any single and more whitespaces
		
		// This doesn't really apply, but this is how you ignore case
		// Pattern replace = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
		
		// trim the string t prepare it for a replace
		
		Matcher regexMatcher = replace.matcher(str2Replace.trim());
		
		// replaceAll replaces all white space with commas
		
		//System.out.println(regexMatcher.replaceAll(", "));
		
	}
	
}