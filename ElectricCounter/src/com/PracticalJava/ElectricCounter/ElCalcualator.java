package com.PracticalJava.ElectricCounter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class ElCalcualator extends JPanel {
	
	private static final long serialVersionUID = 1000L;//bacao je warning 

	// defaultne vrijednosti
	private static double DEFAULT_RATA = 250; // mjesecna akontacijska rata u
												// kunama
	private static double DEFAULT_VT = 1.14; // cijena VT+PDV
	private static double DEFAULT_NT = 0.56; // cijena NT+PDV
	private static double DEFAULT_NAKNADA = 21.75; // naknada za mjernu uslugu i
												// opskrbu
	private static int DEFAULT_VtPocetak = 0; // stanje brojila za VT na poc.
											// mjeseca
	private static int DEFAULT_VtKraj = 100; // -||- VT na kraju mjeseca
	private static int DEFAULT_NtPocetak = 0; // -||- NT na poc.mj.
	private static int DEFAULT_NtKraj = 100; // -||- NT na kraju mj.

	// stringovi za labele
	private static String vtString = "Cijena više tarife s PDV-om (0.01 - 5.00 kn): ";
	private static String ntString = "Cijena niže tafife s PDV-om (0.01 - 5.00 kn): ";
	private static String rataString = "Akontacijska rata sa PDV-om: ";
	private static String naknadaString = "Naknada za uslugu HEP-a: ";
	private static String vtPocetakFieldString = "Poèetak mjeseca - VT: ";
	private static String vtKrajFieldString = "Kraj mjeseca - VT: ";
	private static String ntPocetakFieldString = "Poèetak mjeseca - NT: ";
	private static String ntKrajFieldString = "Kraj mjeseca - NT: ";
	private static String placanjeString = "Iznos za platiti sa PDV-om: ";

	// labele za tekstualna polja
	private JLabel vtLabel;// cijena VT
	private JLabel ntLabel;// cijena NT
	private JLabel rataLabel;// akontacijska rata
	private JLabel naknadaLabel;// naknada
	private JLabel vtPocetakFieldLabel;
	private JLabel vtKrajFieldLabel;
	private JLabel ntPocetaktFieldLabel;
	private JLabel ntKrajFieldLabel;
	private JLabel placanjeLabel;// izracun

	// polja za unos podataka
	private JTextField vtField; // decimal
	private JTextField ntField; // decimal
	private JTextField naknadaField; // decimal
	private JTextField rataField; // money
	private JTextField placanjeField; // payment
	private JTextField vtPocetakField; // integer
	private JTextField vtKrajField; // integer
	private JTextField ntPocetakField; // integer
	private JTextField ntKrajField; // integer

	// Formati za format i parsiranje brojeva
	private NumberFormat integerFormat;
	private DecimalFormat decimalFormat;
	private DecimalFormat paymentFormat;

	private Provjera provjera = new Provjera();


	// konstruktor
	public ElCalcualator() {
		super(new BorderLayout());

		postaviFormat(); //for later implementation!!

		// za izracun mjecesnog iznosa sa default vrijednostima
		 double placanje = izracunaj(DEFAULT_VT, DEFAULT_NT,
		 DEFAULT_VtPocetak, DEFAULT_VtKraj,
		 DEFAULT_NtPocetak, DEFAULT_NtKraj,
		 DEFAULT_NAKNADA, DEFAULT_RATA);

		// double payment = computeForecast(vtField, ntField,
		// vtStartField, ntStartField,
		// vtEndField, ntEndField,
		// paymentField);

		// kreiramo labele
		vtLabel = new JLabel(vtString);
		ntLabel = new JLabel(ntString);
		rataLabel = new JLabel(rataString);// rata
		naknadaLabel = new JLabel(naknadaString);// naknada
		vtPocetakFieldLabel = new JLabel(vtPocetakFieldString);
		vtKrajFieldLabel = new JLabel(vtKrajFieldString);
		ntPocetaktFieldLabel = new JLabel(ntPocetakFieldString);
		ntKrajFieldLabel = new JLabel(ntKrajFieldString);
		placanjeLabel = new JLabel(placanjeString);// iznos

		// kreiraj txtPolja i postavi txtPolja
		// visa tarifa
		vtField = new JTextField(decimalFormat.format(DEFAULT_VT), 10);
		vtField.setInputVerifier(null);

		// niza tarifa
		ntField = new JTextField(decimalFormat.format(DEFAULT_NT), 10);
		ntField.setInputVerifier(null);

		// naknada
		naknadaField = new JTextField(decimalFormat.format(DEFAULT_NAKNADA), 10);
		naknadaField.setInputVerifier(null);

		// akontacijska rata
		rataField = new JTextField(decimalFormat.format(DEFAULT_RATA), 5);
		rataField.setInputVerifier(null);

		// pocetak vise tarife
		vtPocetakField = new JTextField(integerFormat.format(DEFAULT_VtPocetak));
		vtPocetakField.setInputVerifier(null);
		// kraj vise tarife
		vtKrajField = new JTextField(integerFormat.format(DEFAULT_VtKraj));
		vtKrajField.setInputVerifier(null);

		// pocetak nize tarife
		ntPocetakField = new JTextField(integerFormat.format(DEFAULT_NtPocetak));
		ntPocetakField.setInputVerifier(null);
		// kraj nize tarife
		ntKrajField = new JTextField(integerFormat.format(DEFAULT_NtKraj));
		ntKrajField.setInputVerifier(null);

		// rata za platiti
		placanjeField = new JTextField(paymentFormat.format(placanje), 10);
		placanjeField.setInputVerifier(null);
		placanjeField.setEditable(false);
		// makni paymentField iz focus-a
		placanjeField.setFocusable(false);
		placanjeField.setForeground(Color.red);

		// action listener za handle(enter ili tab key na keyboard-u)
		vtField.addActionListener(null);
		ntField.addActionListener(null);
		naknadaField.addActionListener(null);
		rataField.addActionListener(null);
		vtPocetakField.addActionListener(null);
		vtKrajField.addActionListener(null);
		ntPocetakField.addActionListener(null);
		ntKrajField.addActionListener(null);

		// za labela/textField parove
		vtLabel.setLabelFor(vtField);// visa tarifa
		ntLabel.setLabelFor(ntField);// niza tarifa
		naknadaLabel.setLabelFor(naknadaField);// naknada HEP-a
		rataLabel.setLabelFor(rataField);// akontacijaska rata
		vtPocetakFieldLabel.setLabelFor(vtPocetakField);// pocetak mjeseca VT
		vtKrajFieldLabel.setLabelFor(vtKrajField);// kraj mjeseca VT
		ntPocetaktFieldLabel.setLabelFor(ntPocetakField);// pocetak mjeseca NT
		ntKrajFieldLabel.setLabelFor(vtKrajField);// kraj mjeseca NT
		placanjeLabel.setLabelFor(placanjeField);// uplaceni iznos

		// layout za labele u panelu
		JPanel labelPanel = new JPanel(new GridLayout(0, 1));
		labelPanel.add(vtLabel);
		labelPanel.add(ntLabel);
		labelPanel.add(naknadaLabel);
		labelPanel.add(rataLabel);
		labelPanel.add(vtPocetakFieldLabel);
		labelPanel.add(vtKrajFieldLabel);
		labelPanel.add(ntPocetaktFieldLabel);
		labelPanel.add(ntKrajFieldLabel);
		labelPanel.add(placanjeLabel);

		// layout za txtFields u panelu
		JPanel fieldPanel = new JPanel(new GridLayout(0, 1));
		fieldPanel.add(vtField);
		fieldPanel.add(ntField);
		fieldPanel.add(naknadaField);
		fieldPanel.add(rataField);
		fieldPanel.add(vtPocetakField);
		fieldPanel.add(vtKrajField);
		fieldPanel.add(ntPocetakField);
		fieldPanel.add(ntKrajField);
		fieldPanel.add(placanjeField);

		// postavlja panele u frame, labele na lijevo, polja na desno
		setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		add(labelPanel, BorderLayout.CENTER);
		add(fieldPanel, BorderLayout.LINE_END);
	}

	// klasa za provjeru unosa od strane korisnika

	class Provjera extends InputVerifier implements ActionListener {
		
		double MIN_TARIFA = 0.0000;
		double MAX_TARIFA = 10.0000;
		double MIN_RATA = 0.0000;
		double MAX_RATA = 50000.0;
		double MIN_NAKNADA = 000.0;
		double MAX_NAKNADA = 100.0;
		int MIN_START = 0;
		int MAX_END = 99999;
		String msg; // pocetni string potreban za poruke ili poruke
							// prilikom greske
		
		public boolean shouldYieldFocus(JComponent unos) {
			boolean inputOK = verify(unos);
			popravi(unos);
			obnoviPlacanje();
			
			if (inputOK) {
				return true;
			}

			// dijalog sa porukom
			msg += ".\nPokusajte ponovno!"; // string
			JOptionPane.showMessageDialog(null, msg, "Kriva vrijednost",
					JOptionPane.WARNING_MESSAGE);

			//reloadaj input verifier
			unos.setInputVerifier(this);

			//ne zelimo dopustiti fokus u slucaju loseg unosa
			Toolkit.getDefaultToolkit(); //moze ili sa .beep()
			return false;
		}

		//
		public void obnoviPlacanje() {
			double rata = DEFAULT_RATA; // mj.rata
			double naknada = DEFAULT_NAKNADA; // naknada
			double vt = DEFAULT_VT;
			double nt = DEFAULT_NT;
			int vtPocetak = DEFAULT_VtPocetak;
			int vtKraj = DEFAULT_VtKraj;
			int ntPocetak = DEFAULT_NtPocetak;
			int ntKraj = DEFAULT_NtKraj;
			double placanje = 0.0; //iznos za platiti
			
			//parsiramo vrijednosti
			try {
				rata = decimalFormat.parse(rataField.getText()).doubleValue();
				naknada = decimalFormat.parse(naknadaField.getText()).doubleValue();
			} catch (ParseException pe) {}
			try {
				vt = decimalFormat.parse(vtField.getText()).doubleValue();
				nt = decimalFormat.parse(ntField.getText()).doubleValue();
			} catch (ParseException pe) {}
			try {
				vtPocetak = integerFormat.parse(vtPocetakField.getText()).intValue();
				vtKraj = integerFormat.parse(vtKrajField.getText()).intValue();
				ntPocetak = integerFormat.parse(ntPocetakField.getText()).intValue();
				ntKraj = integerFormat.parse(ntKrajField.getText()).intValue();
			} catch (ParseException pe) {}

			// izracunaj potrosnju za gui
			 placanje = izracunaj(vt, nt, vtPocetak, vtKraj, ntPocetak,ntKraj, naknada, rata); //fja racuna iznos za platiti
			 //JOptionPane.showMessageDialog(null, msg,"Ukupan iznos za platiti", 1, icon);
			 placanjeField.setText(paymentFormat.format(placanje));
		}

		// glavna metoda koja provjera unos korisnika za sva polja
		@Override
		public boolean verify(JComponent input) {
			// TODO Auto-generated method stub
			return provjeriPolja(input, false);
		}
		
		protected boolean popravi(JComponent unos) {
			return provjeriPolja(unos, true);
		}

		// protected void popravi(JComponent input) {
		// provjeriPolje(input, true);
		// }
		
		// sluzi za provjeru svih polja u GUI-u
		protected boolean provjeriPolja(JComponent unos, boolean promjeni) {

//			if ((input == vtField) || (input == ntField)) {
//				return provjeriPoljaTarifa(promjeniPolje);
//			}
//			 else if(input == naknadaField) {
//			 return provjeriPoljeNaknade(promjeniPolje);
//			 }
//			 else if(input == rataField) {
//			 return provjeriPoljeAkRate(promjeniPolje);
//			 }
//			 else if((input == vtStartField) || (input == vtEndField)) {
//			 return provjeriPoljaKwh(promjeniPolje);
//			 }
//			 else if((input == ntStartField) || (input == ntEndField)) {
//			 return provjeriPoljaKwh(promjeniPolje);
//			 }
//			else {
				return true;
			}

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			JTextField source = (JTextField) event.getSource();
			shouldYieldFocus(source);
			source.selectAll();
		}
		

		// provjerava tarifna polja kod unosa
//		public boolean provjeriPoljaTarifa(boolean promjeni) {
//			boolean jeTocan = true;
//			double vt = DEFAULT_VT;
//			double nt = DEFAULT_NT;
//			try {
//				vt = decimalFormat.parse(vtField.getText()).doubleValue();
//				nt = decimalFormat.parse(ntField.getText()).doubleValue();
//			} catch (ParseException pe) {
//				msg += "Krivi decimalni format!";
//				return false;
//			}
//			// u slucaju krivog unosa vise tarife
//			if ((vt < MIN_TARIFA) || (vt > MAX_TARIFE)) {
//				jeTocan = false;
//				if (vt < MIN_TARIFA) {
//					msg = "Visa tarifa je manja od "
//							+ decimalFormat.format(MIN_TARIFA);
//				} else {
//					msg = "Visa tarifa je veca od "
//							+ decimalFormat.format(MAX_TARIFA);
//				}
//			}
//			// krivi unos nize tarife
//			else if ((nt < ) || (nt > MAX_TARIFE)) {
//				jeTocan = false;
//				msg = "Niza tarifa je manja od "
//						+ decimalFormat.format(MIN_TARIFE);
//			} else {
//				msg = "Niza tarifa je veca od "
//						+ decimalFormat.format(MAX_TARIFE);
//			}
//			// format na fini decimalni broj
//			if (promjeni) {
//				vtField.setText(decimalFormat.format(vt));
//				vtField.selectAll();
//				ntField.setText(decimalFormat.format(nt));
//				ntField.selectAll();
//			}
//			// fja vraca bool!!
//			return jeTocan;
//		}

		// provjera polja za naknadu kod unosa
		// public boolean provjeriPoljeNaknade(boolean promjeni) {
		// boolean jeTocan = true;
		// double naknada = DEFAULT_FEE;
		//
		// //parsiramo unos
		// try {
		// naknada = decimalFormat.parse(naknadaField.getText()).doubleValue();
		//
		// }catch(ParseException pe) {
		// msg = "Krivi novcani format!";
		// return false;
		// }
		// //krivi unos
		// if((naknada < MIN_RATA) || (naknada > MAX_RATA)) {
		// jeTocan = false;
		// msg = "Krivi unos: Naknada je manja od " + MIN_NAKNADA +
		// "ili veca od " + MAX_NAKNADA;
		// }
		// }

//		public void actionPerformed(ActionEvent event) {
//			JTextField source = (JTextField) event.getSource();
//			//shouldYieldFocus(source);
//			source.selectAll();
//		}
	}
	

	// fja koja ce kreirati GUI
	private static void createAndShowGUI() {
		// kreiraj i postavi prozor
		JFrame frame = new JFrame("Electricity Bill Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JComponent newContentPanel = new ElCalcualator();
		newContentPanel.setOpaque(true);
		frame.setContentPane(newContentPanel);

		// prikazuje prozor
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException exception) {
			// TODO: handle exception
			exception.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		// UIManager.put("swing.boldMetal", Boolean.FALSE);

		// kreira GUI cijele aplikacije pomocu event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}
		});

	}
	
	double izracunaj(double vt, double nt, int vtPocetak, int vtKraj, int ntPocetak, int ntKraj, double naknada, double rata) {
		return 0;
	}
	
	private void postaviFormat() {
		decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance();
		decimalFormat.setParseIntegerOnly(true);
		
		integerFormat = NumberFormat.getIntegerInstance();
		
		paymentFormat = (DecimalFormat)NumberFormat.getNumberInstance();
		paymentFormat.setMaximumFractionDigits(2);
		paymentFormat.setNegativePrefix("(");
		paymentFormat.setNegativeSuffix(")");
	}

}
