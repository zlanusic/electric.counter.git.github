package com.PracticalJava.ElectricCounter;




import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
 
import java.awt.*;
import java.awt.event.*;
 
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
 
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
 
import java.text.*;

public class ElCalcualator extends JPanel {
	
	public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem_new, menuItem_open, menuItem_printPrev, menuItem_print, menuItem_saveAs, menuItem_exit ;
 
        menuBar = new JMenuBar();
 
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription("File Opcije");
        menuBar.add(menu);
 
        //JMenuItems
        menuItem_new = new JMenuItem("New");
        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        menuItem_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
        menuItem_new.getAccessibleContext().setAccessibleDescription("Stvori file");
        menuItem_open = new JMenuItem("Open...");
        menuItem_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
        menuItem_saveAs = new JMenuItem("Save As");
        menuItem_saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItem_exit = new JMenuItem("Exit");
        menuItem_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menu.add(menuItem_new);
        menu.add(menuItem_open);
        menu.add(menuItem_saveAs);
        
        menu.addSeparator();
        
        
        submenu = new JMenu("Print");
        submenu.setMnemonic(KeyEvent.VK_S);
 
        menuItem_printPrev = new JMenuItem("Print preview...");
        submenu.add(menuItem_printPrev);
 
        menuItem_print = new JMenuItem("Print");
        menuItem_print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        submenu.add(menuItem_print);
        menu.add(submenu);
        
        menu.addSeparator();
 
        menu.add(menuItem_exit);
        
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription("Opis aplikacije");
        menuBar.add(menu);
 
        return menuBar;
    }
	
	private static final long serialVersionUID = 1000L;//bacao je warning 

	// defaultne vrijednosti
	private static double DEFAULT_RATA = 250; // mjesecna akontacijska rata u kunama
	private static double DEFAULT_VT = 1.14; // cijena VT+PDV
	private static double DEFAULT_NT = 0.56; // cijena NT+PDV
	private static double DEFAULT_NAKNADA = 21.75; // naknada za mjernu uslugu i
												// opskrbu
	private static int DEFAULT_VtPocetak = 1; // stanje brojila za VT na poc.
											// mjeseca
	private static int DEFAULT_VtKraj = 100; // -||- VT na kraju mjeseca
	private static int DEFAULT_NtPocetak = 1; // -||- NT na poc.mj.
	private static int DEFAULT_NtKraj = 100; // -||- NT na kraju mj.
	
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

	// stringovi za labele
	private static String vtString = "Cijena više tarife s PDV-om (0.1 - 5.00 kn): ";
	private static String ntString = "Cijena niže tafife s PDV-om (0.1 - 5.00 kn): ";
	private static String rataString = "Akontacijska rata sa PDV-om: ";
	private static String naknadaString = "Naknada za uslugu HEP-a: ";
	private static String vtPocetakFieldString = "Poèetak mjeseca - VT: ";
	private static String vtKrajFieldString = "Kraj mjeseca - VT: ";
	private static String ntPocetakFieldString = "Poèetak mjeseca - NT: ";
	private static String ntKrajFieldString = "Kraj mjeseca - NT: ";
	private static String placanjeString = "Iznos za platiti sa PDV-om: ";
		
	// polja za unos podataka
	private JTextField vtField; // decimal
	private JTextField ntField; // decimal
	private JTextField naknadaField; // decimal
	private JTextField rataField; // decimal
	private JTextField placanjeField; // payment
	private JTextField vtPocetakField; // integer
	private JTextField vtKrajField; // integer
	private JTextField ntPocetakField; // integer
	private JTextField ntKrajField; // integer

	// Formati za format i parsiranje brojeva
	private NumberFormat novcaniFromat;
	private NumberFormat percentFormat;
	private NumberFormat integerFormat;
	private DecimalFormat decimalFormat;
	private DecimalFormat paymentFormat;
	private Provjera provjera = new Provjera();


	// konstruktor
	public ElCalcualator() {
		super(new BorderLayout());

		postaviFormat(); 

		 // za izracun mjecesnog iznosa sa default vrijednostima
		 double placanje = izracunaj(DEFAULT_VT, DEFAULT_NT,
		 DEFAULT_VtPocetak, DEFAULT_VtKraj,
		 DEFAULT_NtPocetak, DEFAULT_NtKraj,
		 DEFAULT_NAKNADA, DEFAULT_RATA);		 
		 
		
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
		vtField = new JTextField(percentFormat.format(DEFAULT_VT), 10);
		vtField.putClientProperty("JComponent.sizeVariant", "large");
		vtField.setInputVerifier(provjera);

		// niza tarifa
		ntField = new JTextField(percentFormat.format(DEFAULT_NT), 10);
		ntField.putClientProperty("JComponent.sizeVariant", "large");
		ntField.setInputVerifier(provjera);

		// naknada
		naknadaField = new JTextField(percentFormat.format(DEFAULT_NAKNADA), 10);
		naknadaField.putClientProperty("JComponent.sizeVariant", "large");
		naknadaField.setInputVerifier(provjera);

		// akontacijska rata
		rataField = new JTextField(percentFormat.format(DEFAULT_RATA), 5);
		rataField.putClientProperty("JComponent.sizeVariant", "large");
		rataField.setInputVerifier(provjera);

		// pocetak vise tarife
		vtPocetakField = new JTextField(decimalFormat.format(DEFAULT_VtPocetak));
		vtPocetakField.putClientProperty("JComponent.sizeVariant", "large");
		vtPocetakField.setInputVerifier(provjera);
		// kraj vise tarife
		vtKrajField = new JTextField(decimalFormat.format(DEFAULT_VtKraj));
		vtKrajField.putClientProperty("JComponent.sizeVariant", "large");
		vtKrajField.setInputVerifier(provjera);

		// pocetak nize tarife
		ntPocetakField = new JTextField(decimalFormat.format(DEFAULT_NtPocetak));
		ntPocetakField.putClientProperty("JComponent.sizeVariant", "large");
		ntPocetakField.setInputVerifier(provjera);
		// kraj nize tarife
		ntKrajField = new JTextField(decimalFormat.format(DEFAULT_NtKraj));
		ntKrajField.putClientProperty("JComponent.sizeVariant", "large");
		ntKrajField.setInputVerifier(provjera);

		// rata za platiti
		placanjeField = new JTextField(paymentFormat.format(placanje), 10);
		placanjeField.putClientProperty("JComponent.sizeVariant", "large");
		placanjeField.setInputVerifier(provjera);
		//placanjeField.setEditable(false);
		// makni paymentField iz focus-a
		placanjeField.setFocusable(false);
		placanjeField.setForeground(Color.red);

		// action listener za handle(enter ili tab key na keyboard-u)
		vtField.addActionListener(provjera);
		ntField.addActionListener(provjera);
		naknadaField.addActionListener(provjera);
		rataField.addActionListener(provjera);
		vtPocetakField.addActionListener(provjera);
		vtKrajField.addActionListener(provjera);
		ntPocetakField.addActionListener(provjera);
		ntKrajField.addActionListener(provjera);

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
		
		double MIN_TARIFA = 0.1;
		double MAX_TARIFA = 10.0;
		double MIN_RATA = 1.0;
		double MAX_RATA = 50000.0;
		double MIN_NAKNADA = 1;
		double MAX_NAKNADA = 100;
		int MIN_START = 1;
		int MAX_END = 99999;
		String msg = null; // pocetni string potreban za poruke ili poruke
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
			JOptionPane.showMessageDialog(null, msg, "Kriva vrijednost", JOptionPane.WARNING_MESSAGE);

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
				rata = percentFormat.parse(rataField.getText()).doubleValue();			
			} catch (ParseException pe) {}
			try {
				naknada = percentFormat.parse(naknadaField.getText()).doubleValue();
			} catch (ParseException pe) {}
			try {
				vt = percentFormat.parse(vtField.getText()).doubleValue();
				nt = percentFormat.parse(ntField.getText()).doubleValue();
			} catch (ParseException pe) {}
			try {
				vtPocetak = decimalFormat.parse(vtPocetakField.getText()).intValue();
				vtKraj = decimalFormat.parse(vtKrajField.getText()).intValue();
				ntPocetak = decimalFormat.parse(ntPocetakField.getText()).intValue();
				ntKraj = decimalFormat.parse(ntKrajField.getText()).intValue();
			} catch (ParseException pe) {}

			// izracunaj potrosnju za gui
			 placanje = izracunaj(vt, nt, vtPocetak, vtKraj, ntPocetak, ntKraj, naknada, rata); //fja racuna iznos za platiti
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
		
		// sluzi za provjeru svih polja u GUI-u
		protected boolean provjeriPolja(JComponent unos, boolean promjeni) {

			if (unos == vtField) {
				return provjeriTarifu1(promjeni);
			}
			else if (unos == ntField) {
				return provjeriTarifu2(promjeni);
			}
			 else if(unos == naknadaField) {
			 return provjeriNaknadu(promjeni);
			 }
			 else if(unos == rataField) {
			 return provjeriRatu(promjeni);
			 }
			 else if((unos == vtPocetakField) || (unos == vtKrajField)) {
			 return provjeriKwh(promjeni);
			 }
			 else if((unos == ntPocetakField) || (unos == ntKrajField)) {
			 return provjeriKwh(promjeni);
			 }
			else {
				return true;
			 }
		}

		

		// provjerava tarifna polja kod unosa
		public boolean provjeriTarifu1(boolean promjena) {
			boolean jeTocan = true;
			double vt = DEFAULT_VT;
			
			//parsiranje
			try {
				vt = percentFormat.parse(vtField.getText()).doubleValue();
			} catch (ParseException pe) {
				msg += "Krivi decimalni format!";
				return false;
			}
			
			// u slucaju krivog unosa
			if ((vt < MIN_TARIFA) || (vt > MAX_TARIFA)) {
				jeTocan = false;
				if (vt < MIN_TARIFA) {
					msg = "Visa tarifa je manja od " + (MIN_TARIFA) + ".\nStavite zarez!";
				} 
				else { //ako je veca od max. vrijednosti
					msg = "Visa tarifa je veca od " + (MAX_TARIFA);
				}
			}
			
			// format na fini decimalni broj
			if (promjena) {
				vtField.setText(percentFormat.format(vt));
				vtField.selectAll();
			}
			// fja vraca istinitu bool vrijednost!!
			return jeTocan;
		}
		
		public boolean provjeriTarifu2(boolean promjeni) {
			double nt = DEFAULT_NT;
			boolean jeTocan = true;
			
			try {
				nt = percentFormat.parse(ntField.getText()).doubleValue();
			} catch (ParseException pe) {
				// TODO: handle exception
				msg += "Krivi decimalni format!";
				return false;
			}
			
			if ((nt < MIN_TARIFA ) || (nt > MAX_TARIFA)) {
				jeTocan = false;
				if (nt < MIN_TARIFA) {
					msg = "Niza tarifa je manja od " + (MIN_TARIFA);
				}
			}
			else {
				msg = "Niza tarifa je veca od " + (MAX_TARIFA);
			}
			
			if (promjeni) {
				ntField.setText(percentFormat.format(nt));
				ntField.selectAll();
			}
			
			return jeTocan;

		}

		// provjera polja za naknadu kod unosa
		public boolean provjeriNaknadu(boolean promjeni) {
			boolean jeTocan = true;
			double naknada = DEFAULT_NAKNADA;

			// parsiramo unos
			try {
				naknada = percentFormat.parse(naknadaField.getText())
						.doubleValue();

			} catch (ParseException pe) {
				msg = "Krivi decimalni format!";
				return false;
			}
			// krivi unos
			if ((naknada < MIN_NAKNADA) || (naknada > MAX_NAKNADA)) {
				jeTocan = false;
				if (naknada < MIN_NAKNADA) {
					msg = "Naknada je manja od "
							+ (MIN_NAKNADA);
				} else {
					msg = "Naknada je veca od "
							+ (MAX_NAKNADA);
				}
			}
			
			if (promjeni) {
				naknadaField.setText(percentFormat.format(naknada));
				naknadaField.selectAll();
			}
			
			return jeTocan;
		}
		
		public boolean provjeriRatu(boolean promjena) {
			boolean jeTocan = true;
			double rata = DEFAULT_RATA;
			
			try {
				rata = novcaniFromat.parse(rataField.getText()).doubleValue();
			} catch (ParseException pe) {
				// TODO: handle exception
				msg = "Krivi format u polju rate!";
				return false;
			}
			
			if ((rata < MIN_RATA) || (rata > MAX_RATA)) {
				jeTocan = false;
				if (rata < MIN_RATA) {
					msg = "Rata je manja od " + integerFormat.format(MIN_RATA);
				}else {
					msg = "Rata je veca od " + integerFormat.format(MAX_RATA);
				}
			}
			if (promjena) {
				rataField.setText(novcaniFromat.format(rata));
				rataField.selectAll();
			}
			
			return jeTocan;
		}
		
		public boolean provjeriKwh(boolean promjena) {
			boolean jeTocan = true;
			int vtPoc = DEFAULT_VtPocetak;
			int ntPoc = DEFAULT_NtPocetak;
			int vtKraj = DEFAULT_NtPocetak;
			int ntKraj = DEFAULT_NtKraj;
			
			try {
				vtPoc = decimalFormat.parse(vtPocetakField.getText()).intValue();
				vtKraj = decimalFormat.parse(vtKrajField.getText()).intValue();
				ntPoc = decimalFormat.parse(ntPocetakField.getText()).intValue();
				ntKraj = decimalFormat.parse(ntKrajField.getText()).intValue();
			} catch (ParseException pe) {
				// TODO: handle exception
				msg = "Krivi format u Kwh poljima";
				return false;
			}
			
			if ((vtPoc < MIN_START) || (vtPoc > MAX_END)) {
				jeTocan = false;
				if(vtPoc < MIN_START){
				msg = "Unos je manji od " + integerFormat.format(MIN_START) + "Kwh";
				}
				else {
					msg = "Unos je veci od " + integerFormat.format(MAX_END) + "Kwh";
				}
			}
			if ((ntPoc < MIN_START) || (ntPoc > MAX_END)) {
				jeTocan = false;
				if (ntPoc < MIN_START) {
					msg = "Unos je manji od " + integerFormat.format(MIN_START);
				}
				else {
					msg = "Unos je veci od " + integerFormat.format(MAX_END);
				}
				
			}
			
			if ((vtKraj < MIN_START) || (vtKraj > MAX_END)) {
				jeTocan = false;
				if (vtKraj < MIN_START) {
					msg = "Unos je manji od " + integerFormat.format(MIN_START);
				}
				else {
					msg = "Unos je veci od " + integerFormat.format(MAX_END);
				}
			}
			
			if ((ntKraj < MIN_START) || (ntKraj > MAX_END)) {
				jeTocan = false;
				if (ntKraj < MIN_START) {
					msg = "Unos je manji od " + integerFormat.format(MIN_START);
				}
				else {
					msg = "unos je veci od " + integerFormat.format(MAX_END); 
				}
			}
			
			if (promjena) {
				vtKrajField.setText(decimalFormat.format(vtKraj));
				vtKrajField.selectAll();
			}
			
			if (promjena) {
				ntKrajField.setText(decimalFormat.format(ntKraj));
				ntKrajField.selectAll();
			}
				
			if (promjena) {
				vtPocetakField.setText(decimalFormat.format(vtPoc));
				vtPocetakField.selectAll();
			}
			
			if(promjena) {
				ntPocetakField.setText(decimalFormat.format(ntPoc));
				ntPocetakField.selectAll();
			}
			
			return jeTocan;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			JTextField source = (JTextField) event.getSource();
			shouldYieldFocus(source);
			source.selectAll();
		}

	}	

	// fja koja ce kreirati GUI
	private static void createAndShowGUI() {
		// kreiraj i postavi prozor
		JFrame frame = new JFrame("Kalkulator potrosnje struje");
		ElCalcualator demoCalcualator = new ElCalcualator();
		frame.setJMenuBar(demoCalcualator.createMenuBar());
		SwingUtilities.updateComponentTreeUI(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		
		JComponent newContentPanel = new ElCalcualator();
		newContentPanel.setOpaque(true);
		frame.setContentPane(newContentPanel);
		
	
		
		
		// prikazuje prozor
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(true);
		
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); //ili javax.swing.plaf.metal.MetalLookAndFeel
			//UIManager.put("nimbusBlueGray", new Color(15, 35, 67, 67));
			
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

		// UIManager.put("swing.boldMetal", Boolean.FALSE); //slova nisu boldana

		// kreira GUI cijele aplikacije pomocu event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}
		});

	}
	
	double izracunaj(double vt, double nt, int vtPocetak, int vtKraj, int ntPocetak, int ntKraj, double naknada, double rata) {
		double t1, t2, kn1, kn2, iznosKn, ukupno;
		
		t1 = vtKraj - vtPocetak;
		t2 = ntKraj - ntPocetak;
		
		kn1 = t1 * vt;
		kn2 = t2 * nt;
		
		iznosKn = kn1 + kn2;
		ukupno = iznosKn + naknada + rata;
		return ukupno;
	}
	
	private void postaviFormat() {
		novcaniFromat = (NumberFormat)NumberFormat.getNumberInstance();
		
		percentFormat = NumberFormat.getNumberInstance();
		percentFormat.setMinimumFractionDigits(2);
		
		decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
		decimalFormat.setParseIntegerOnly(true);
		
		integerFormat = NumberFormat.getIntegerInstance();
		
		paymentFormat = (DecimalFormat)NumberFormat.getNumberInstance();
		paymentFormat.setMaximumFractionDigits(2);
		paymentFormat.setNegativePrefix("(");
		paymentFormat.setNegativeSuffix(")");
	}

}
