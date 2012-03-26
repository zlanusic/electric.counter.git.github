package com.PracticalJava.ElectricCounter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;





public class ElCalcualator extends JPanel {

	/**
	 * @param args
	 */
	
	//Default vaules
	private static double DEFAULT_RATE = 250; // mjesecna akontacijska rata(kn)
	private static double DEFAULT_VT = 0.9150; //cijena VT+PDV
	private static double DEFAULT_NT = 0.4636; //cijena NT+PDV
	private static double DEFAULT_FEE = 19.6800; //naknada za mjernu uslugu i opskrbu
	private static int DEFAULT_VTSTART = 0; //stanje brojila za VT na poc. mjeseca
	private static int DEFAULT_VTEND = 100; //     -||-           VT na kraju mjeseca 
	private static int DEFAULT_NTSTART = 0; //   -||-           NT na poc.mj.
	private static int DEFAULT_NTEND = 100; //     -||-           NT na kraju mj.
	
	//String for labels
	private static String highRateString = "Visa Tarifa s PDV-om (0.01 - 5.00 kn):";
	private static String lowRateString = "Nisa Tarifa s PDV-om (0.01 - 5.00 kn):";
	private static String paymentString = "rata za platiti ili rata pretplate:";
	private static String vtStartFieldString = "Pocetak mjeseca - VT:";
	private static String vtEndFieldString = "Kraj mjeseca - VT:";
	private static String ntStartFieldString = "Pocetak mjeseca - NT:";
	private static String ntEndFieldString = "Kraj mjeseca - NT:";
	private static String feeString = "Naknada za uslugu HEP-a:";
	private static String feeRateString = "Mjesecna rata:";

	//Labels for text fields identity
	private JLabel highRateLabel;
	private JLabel lowRateLabel;
	private JLabel paymentLabel;
	private JLabel vtStartFieldLabel;
	private JLabel vtEndFieldLabel;
	private JLabel ntStartFieldLabel;
	private JLabel ntEndFieldLabel;
	private JLabel feeLabel;
	private JLabel rateFeeLabel;
	
	
	//Text fields for data input
	private JTextField vtField; //decimal
	private JTextField ntField; //decimal
	private JTextField feeField; //money
	private JTextField rateField; //money
	private JTextField paymentField; //payment
	private JTextField vtStartField; //decimal
	private JTextField ntStartField; //decimal
	private JTextField vtEndField; //decimal
	private JTextField ntEndField; //decimal
	
	
	//Formats to format and parse numbers
	private NumberFormat doubleFormat;
	private NumberFormat moneyFormat;
	private DecimalFormat decimalFormat;
	private DecimalFormat paymentFormat;
	private NumberFormat integerFormat;
	//private Provjera check = new Provjera();
	
	//konstruktor
	public ElCalcualator() {
		super(); 
		this.setVisible(true);
		
		//setUpFormats(); //for later implementation!!
		
		//for calculate mounthly payment but with default values
		double payment = computeForecast(DEFAULT_NT, DEFAULT_VT,
										 DEFAULT_NTSTART, DEFAULT_VTSTART,
										 DEFAULT_NTEND, DEFAULT_VTEND,
										 DEFAULT_FEE, DEFAULT_VT);
		
//		double payment = computeForecast(vtField, ntField, 
//									    vtStartField, ntStartField,
//									    vtEndField, ntEndField, 
//									    paymentField);
		
		//Create the labels
		highRateLabel = new JLabel(highRateString);
		lowRateLabel = new JLabel(lowRateString);
		paymentLabel = new JLabel(paymentString);
		vtStartFieldLabel = new JLabel(vtStartFieldString);
		vtEndFieldLabel = new JLabel(vtEndFieldString);
		ntStartFieldLabel = new JLabel(ntStartFieldString);
		ntEndFieldLabel = new JLabel(ntEndFieldString);
		feeLabel = new JLabel(feeString);
		rateFeeLabel = new JLabel(feeRateString);
		
		//Create Text fields and set up them
		//visa tarifa
		vtField = new JTextField(decimalFormat.format(DEFAULT_VT), 10);
		vtField.setInputVerifier(check); //verifier is the function which we'll use for later!!
		
		//niza tarifa
		ntField = new JTextField(decimalFormat.format(DEFAULT_NT), 10);
		ntField.setInputVerifier(check);
		
		//naknada
		feeField = new JTextField(moneyFormat.format(DEFAULT_FEE), 10);
		feeField.setInputVerifier(check);
		
		//akontacijska rata
		rateField = new JTextField(moneyFormat.format(DEFAULT_VT), 5);
		rateField.setInputVerifier(check);
		
		//pocetak vise tarife
		vtStartField = new JTextField(integerFormat.format(DEFAULT_VTSTART));
		vtStartField.setInputVerifier(verifier);
		//kraj vise tarife
		vtEndField = new JTextField(integerFormat.format(DEFAULT_VTEND));
		vtEndField.setInputVerifier(verifier);
		//pocetak nize tarife
		ntStartField = new JTextField(integerFormat.format(DEFAULT_NTSTART));
		ntStartField.setInputVerifier(verifier);
		//kraj nize tarife
		ntEndField = new JTextField(integerFormat.format(DEFAULT_NTEND));
		ntEndField.setInputVerifier(verifier);			
		
		//rata za platiti ili povrat u slucaju pretplate
		//paymentField = new JTextField(paymentFormat.format(payment), 10);
		//paymentField.setInputVerifier(verifier);
		//paymentField.setEditable(false);
		
		//remove component from the focus cycle
		//paymentField.setFocusable(false);
		//paymentField.setForeground(Color.red);
		
		//action listener for handle(return or tab key on keyboard)
		
		
		//for label/textfield pairs
		highRateLabel.setLabelFor(vtField);
		lowRateLabel.setLabelFor(ntField);
		feeLabel.setLabelFor(feeField);
		rateFeeLabel.setLabelFor(rateField);
		vtStartFieldLabel.setLabelFor(vtStartField);
		vtEndFieldLabel.setLabelFor(vtEndField);
		ntStartFieldLabel.setLabelFor(ntStartField);
		ntEndFieldLabel.setLabelFor(ntEndField);
		//paymentLabel.setLabelFor(paymentField);
		
		
		//layout for labels in panel
		JPanel lblPane = new JPanel(new GridLayout(0, 1));
		lblPane.add(highRateLabel);
		lblPane.add(lowRateLabel);
		lblPane.add(feeLabel);
		lblPane.add(rateField);
		lblPane.add(vtStartFieldLabel);
		lblPane.add(vtEndFieldLabel);
		lblPane.add(ntStartFieldLabel);
		lblPane.add(ntEndFieldLabel);
		//lblPane.add(paymentLabel);
		
		//layout for fields in panel
		JPanel txtfdPane = new JPanel(new GridLayout(0, 1));
		txtfdPane.add(vtField);
		txtfdPane.add(ntField);
		txtfdPane.add(feeField);
		txtfdPane.add(rateField);
		txtfdPane.add(vtStartField);
		txtfdPane.add(vtEndField);
		txtfdPane.add(ntStartField);
		txtfdPane.add(ntEndField);
		//txtfdPane.add(paymentField);
		
		//put the panels in this frame, labels on left, txtField on right
		setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		add(lblPane, BorderLayout.CENTER);
		add(txtfdPane, BorderLayout.LINE_END);
}
	//klasa za provjeru unosa od strane korisnika
	class Provjera extends InputVerifier implements ActionListener {
		
		public Provjera() {
			super();
			ImageIcon pic = new ImageIcon();
			pic.setImage(null);
			pic.setDescription("MONEY");
		}
		double MIN_TARIFE = 0.0000;
		double MAX_TARIFE = 15.0000;
		double MIN_RATA = 0.0000;
		double MAX_RATA = 50000.0;
		double MIN_NAKNADA = 000.0; 
		double MAX_NAKNADA = 1000.0; 
		int MIN_START = 0;
		int MAX_END  = 99999;
		String msg = null; //pocetni string potreban za poruke ili poruke prilikom greske
		
		public boolean shouldYieldFocus(JComponent input) {
			//
			boolean inputOK = verify(input); //verify je metoda iz 
			//
			popravi(input);
			updatePayment();
			
			if(inputOK) {
				return true; // ako je unos OK fja vraca true
			}
			//pop up poruka za prozor 
			msg += ".\nPokusajte ponovno!"; //string
			JOptionPane.showMessageDialog(null, msg, "Invalid Value", JOptionPane.WARNING_MESSAGE);
			
			//reload the input verifier
			input.setInputVerifier(this);
			
			//ne zelimo dopustiti fokus u slucaju loseg unosa
			Toolkit.getDefaultToolkit(); //ili sa .beep()
			return false;
		}
		//
		protected void updatePayment() {
			double feeRate = DEFAULT_RATE; //mj.rata
			double fee = DEFAULT_FEE; //naknada
			double hiRate = DEFAULT_VT;
			double loRate = DEFAULT_NT;
			int hiStart = DEFAULT_VTSTART;
			int hiEnd = DEFAULT_VTEND;
			int loStart = DEFAULT_NTSTART;
			int loEnd = DEFAULT_NTEND;
			double payment = 0.0; //iznos za platiti
			
			
			
			try {
				feeRate = moneyFormat.parse(rateField.getText()).doubleValue();
				fee = moneyFormat.parse(feeField.getText()).doubleValue();
			} catch(ParseException pe) { }
			try {
					hiRate = decimalFormat.parse(vtField.getText()).doubleValue();
					loRate = decimalFormat.parse(ntField.getText()).doubleValue();
			} catch(ParseException per) { }
			try {
 					hiStart = integerFormat.parse(vtStartField.getText()).intValue();
 					hiEnd = integerFormat.parse(vtEndField.getText()).intValue();
 					loStart = integerFormat.parse(ntStartField.getText()).intValue();
 					loEnd = integerFormat.parse(ntEndField.getText()).intValue();
 			} catch(ParseException perr) {}
			
			//calculate payment za gui
			payment = computeForecast(feeRate, fee, hiRate, loRate, hiStart, hiEnd, loStart, loEnd); //fja racuna iznos za platiti
			JOptionPane.showMessageDialog(null, msg, "Ukupan iznos za platiti", 1, icon);
			//paymentField.setText(paymentFormat.format(payment));
		}
		//glavna metoda koja provjera unos korisnika za sva polja
		protected boolean provjeri(JComponent input) {
			//zovemo metodu zaduzenu za provjeru unosa u polja(field)
			return provjeriPolje(input, false);
		}
		
		protected void popravi(JComponent input) {
			provjeriPolje(input, true);
		}
		//sluzi za provjeru bilo kojeg polja
		protected boolean provjeriPolje(JComponent input, boolean promjeniPolje) {
			
			if((input == vtField) || (input == ntField)) {
				return provjeriPoljaTarifa(promjeniPolje);
			} 
			else if(input == feeField) {
				return provjeriPoljeNaknade(promjeniPolje);
			}
			else if(input == rateField) {
				return provjeriPoljeAkRate(promjeniPolje);
			}
			else if((input == vtStartField) || (input == vtEndField)) {
				return provjeriPoljaKwh(promjeniPolje);
			}
			else if((input == ntStartField) || (input == ntEndField)) {
				return provjeriPoljaKwh(promjeniPolje);
			}
			else {
				return true;
			}
		}
		//provjerava tarifna polja kod unosa 
		public boolean provjeriPoljaTarifa(boolean promjeni) {
			boolean jeValidan = true;
			double vt = DEFAULT_VT;
			double nt = DEFAULT_NT;
			try {
				vt = decimalFormat.parse(vtField.getText()).doubleValue();
				nt = decimalFormat.parse(ntField.getText()).doubleValue();
			} 
			catch(ParseException pe) {
				msg += "Krivi decimalni format!"; 
				return false;
			}
			//u slucaju krivog unosa
			if((vt < MIN_TARIFE) || (vt > MAX_TARIFE)) {
				jeValidan = false;
				if(vt < MIN_TARIFE) {
					msg = "Visa tarifa je manja od " + doubleFormat.format(MIN_TARIFE);
				} else {
					msg = "Visa tarifa je veca od " + doubleFormat.format(MAX_TARIFE);
				}
			}
			//krivi unos
			else if((nt < MIN_TARIFE) || (nt > MAX_TARIFE)) {
				jeValidan = false;
				msg = "Niza tarifa je manja od " + doubleFormat.format(MIN_TARIFE);
			} else {
				msg = "Niza tarifa je veca od " + doubleFormat.format(MAX_TARIFE);
			}
			//format na fini decimalni broj
			if(promjeni) {
				vtField.setText(decimalFormat.format(vt));
				vtField.selectAll();
				ntField.setText(decimalFormat.format(nt));
				ntField.selectAll();
			}
			//fja vraca bool!!
			return jeValidan;
		}
		//provjera polja za naknadu kod unosa 
		public boolean provjeriPoljeNaknade(boolean promjeni) {
			boolean jeValidan = true;
			double rata = DEFAULT_RATE;
			
			//parsiramo vrijednosti
			try {
				rata = moneyFormat.parse(feeField.getText()).doubleValue();
				
			}catch(ParseException pe) {
				msg = "Krivi novcani format!";
				return false;
			}
			//kriva vrijednost
			if((rata < MIN_RATA) || (rata > MAX_RATA)) {
				
			}
		}
		
		
		
			
	}
		
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
