<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="mi.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MIT</title>
</head>
<body bgcolor="#FFFFFF" link="orange">
<div id="header"  style="background-color:#984242; clear:both;text-align:center;">
<font size="6" face="calibri" color="#FFFFFF"><center>MIT - METER IDENTIFYING TOOL</center></font></div>
<font face="calibri">
<b>Input Verse:</b>
<form action="process.jsp">
<textarea rows="3" cols="100" id="verse" name="verse"></textarea><br>
<form action="">
<input type="radio" name="1" value="1">quarter verse(first pAda)  &nbsp;
<input type="radio" name="2" value="2">half verse (two pAdas)
<input type="radio" name="3" value="3">full verse&nbsp;
<input type="radio" name="4" value="4"> two and more verses&nbsp;
<input type="radio" name="5" value="5">verse with prose&nbsp;

<input type="submit" value="Submit">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="enco.jpg" target="_blank">Click here</a> for SLP1 Encoding.
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="index.html">Home</a>
</form>
</form>
</font>

<table border="1" >
<font > 
<tr>
<td valign="baseline" width="50%" bgcolor="white">
<font face="calibri"> 
<%
MeterIdentification mi = new MeterIdentification();
String output = mi.process(request.getParameter("verse"));
%>
<b>Given Input : </b><br><%=output %> 
</font>
</td>
<td valign="baseline" width="50%" bgcolor="white">
<font face="calibri" ><B>Examples: </b><br>
1. upendravajrā - samavṛttam<br>           
<font color="#1F62B0">
परोपकाराय फलन्ति वृक्षाः परोपकाराय वहन्ति नद्यः| <br>
परोपकाराय दुहन्ति गावः परोपकारार्थमिदं शरीरम् || <br></font>
<font color=black>In SLP1:</font> &nbsp;&nbsp;
<font color=#B74E11>paropakArAya Palanti vfkzAH paropakArAya vahanti nadyaH|<br> 
paropakArAya duhanti gAvaH paropakArArTamidaM SarIram||<br><br>
</font>
2. vegavatī - ardhasamavṛttam <br>
<font color="#1F62B0">
स्मरवेगवती व्रजरामा केशववंशरवैरतिमुग्धा| <br>
रभसान्न गुरून् गणयन्ती केलिनिकुञ्जगृहाय जगाम|| <br></font>
<font color=black>In SLP1:</font>&nbsp;&nbsp;
<font color="#B74E11">smaravegavatI vrajarAmA keSavavaMSaravEratimugDA| <br>
raBasAnna gurUn gaRayantI kelinikuYjagfhAya jagAma||<br><br></font>
3. saurabhakam – viṣamasamavṛttam <br>
<font color="#1F62B0">चरणत्रयं भवति लक्ष्म यदि सकलमुद्गतागतम् | <br>
र्नौ भगौ भवति सौरभकं चरणे यदीह भवतस्तृतीयके || </br></font>
<font color=black>In SLP1:</font> &nbsp;&nbsp;
<font color="#B74E11">caraRatrayaM Bavati lakzma yadi sakalamudgatAgatam| <br>
rnO BagO Bavati sOraBakaM caraRe yadIha BavatastftIyake||<br><br></font>
4. āryā - mātravṛttam <br>
<font color="#1F62B0">आद्यं दलं समस्तं भजेत लक्ष्म चपलागतं यस्याः |<br>
 शेषे पूर्वजलक्ष्मा मुखचपला सोदिता मुनिना ||<br></font>
<font color=black>In SLP1:</font> &nbsp;&nbsp;
<font color="#B74E11">AdyaM dalaM samastaM Bajeta lakzma capalAgataM yasyAH |<br> 
Seze pUrvajalakzmA muKacapalA soditA muninA||</font>
<br> <br><b>Short-cuts in the output:</b><br>
y= yagana, m=magana, t=tagana, r=ragana, j=jagana, B=Bagana, n=nagana,<br>
s=sagana, l/L=laghu, g/G=guru
</font>
</td>


</tr>
</table>

<div id="footer" style="background-color:#984242;clear:both;text-align:center;">
<font face="calibri" color="FFFFFF">
Copyright © Keshav Melnad 2013</font></div>

</body>
</html>