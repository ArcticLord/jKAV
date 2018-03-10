package com.arcticlord.jkav.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;



public class JKAVTable extends JTable{

	private static final long serialVersionUID = 8051772577380508616L;
	
	private JTextField filterText;
	private TableRowSorter<TableModel> sorter;
	private AbstractTableModel model;	
	private ITableSelectionListener selectionListener;	
	private ArrayList<Object[]> data;
	private ContentDescription contentDescription;
	
	
	public JKAVTable(ContentDescription description){
		
		contentDescription = description;
		model = new MyTableModel(contentDescription.getColumnNames());
		sorter = new MyTableRowSorter<TableModel>(model,
				contentDescription.getIntSortedColumns());
		selectionListener = null;
		data = null;
		
		setModel(model);
		setRowSorter(sorter);
	    setFillsViewportHeight(true); 
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				JTable target = (JTable)e.getSource();
				int row = target.getSelectedRow();
				// if no row was selected do nothing
				if(row == -1) return;
				int realRow = convertRowIndexToModel(row);

				if (e.getClickCount() == 1) {
					if(selectionListener != null)
						selectionListener.clicked(realRow);
				}
				
				if (e.getClickCount() == 2) {
					if(selectionListener != null)
						selectionListener.doubleClicked(realRow);
				}
			}
		});

		
		filterText = new JTextField();
		filterText.setToolTipText("Text eingeben um Auswahl zu beschraenken");
	        //Whenever filterText changes, invoke filterTableContent.
	    filterText.getDocument().addDocumentListener( new DocumentListener() {
	    	public void changedUpdate(DocumentEvent e) {
	    		filterTableContent();
	        }
	    	public void insertUpdate(DocumentEvent e) {
	    		filterTableContent();
	        }
	        public void removeUpdate(DocumentEvent e) {
	        	filterTableContent();
	        }
	    });
	}
	
	public JTextField getFilterText(){
		return filterText;
	}
	
	public void setData(ArrayList<Object[]> d){
		data = d;
		model.fireTableDataChanged();
	}
	
	
	public void setSelectionListener(ITableSelectionListener l){
		selectionListener = l;
	}
	
	public void setCellReplacement(String columnName, CellReplacementRule[] rules){
		ColorRenderer cr = new JKAVTable.ColorRenderer(columnName, rules);
		// Statusss ausprobiert -> führt zur exception
		getColumn(columnName).setCellRenderer(cr);
	}
	
	/** 
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void filterTableContent() {
        RowFilter<TableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter("(?i)" + filterText.getText(),
            		contentDescription.getFilteredColumns());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    /**
     * This class stores all important informations about the
     * content that is used in this table.
     */
	public static class ContentDescription{
		private ArrayList<String> columnNames;
		private ArrayList<Integer> filteredColumns;
		private ArrayList<Integer> intSortedColumns;
		
		public ContentDescription(){
			columnNames = new ArrayList<String>();
			filteredColumns = new ArrayList<Integer>();
			intSortedColumns = new ArrayList<Integer>();
		}
		public void addDescription(String name, boolean filtered, boolean intSort){
			columnNames.add(name);
			if(filtered)
				filteredColumns.add(columnNames.size() - 1);
			if(intSort)
				intSortedColumns.add(columnNames.size() - 1);
		}
		public String [] getColumnNames(){
			String [] res = new String[columnNames.size()];
			int pos = 0;
			for(String str : columnNames)
				res[pos++] = str;
			return res;
		}
		public int [] getFilteredColumns(){
			int [] res = new int[filteredColumns.size()];
			int pos = 0;
			for(Integer i : filteredColumns)
				res[pos++] = i;
			return res;
		}
		public int [] getIntSortedColumns(){
			int [] res = new int[intSortedColumns.size()];
			int pos = 0;
			for(Integer i : intSortedColumns)
				res[pos++] = i;
			return res;
		}
	}
	
	
	public static class CellReplacementRule{
		public int realContent;
		public String replacementText;
		public Color backgroundColor;
		public CellReplacementRule(int c, String r, Color b){
			realContent = c;
			replacementText = r;
			backgroundColor = b;
		}
	}
    
	/**
	 * This is a modified table row sorter object that is used to sort the rows
	 * of a table after clicking on the header of one column. The sorting is done
	 * numerically if the chosen column is in the interger sorted list. Otherwise
	 * it is done alphabetically.  
	 * @param <M>	Class type of the table model to use with this row sorter. 
	 */
    private class MyTableRowSorter<M extends TableModel> extends TableRowSorter<M>{
    	private int [] intSortedColumns;
    	/**
    	 * 
    	 * @param m			The table model to use with this row sorter.
    	 * @param intCols	The list of columns that have integer content and
    	 * 					should be sorted numerically.
    	 */
    	public MyTableRowSorter(M m, int [] intCols){
    		super(m);
    		intSortedColumns = intCols;
    	}
    	/**
    	 *  Creates an comparator object that compares the content of two elements. It
    	 *  is used to sort the rows of the table for one column.
    	 *  @param column	The ID of the column that is going to be resorted. 
    	 *  @return 		The comparator object to use for comparing elements while sorting.
    	 */
    	@Override
    	public Comparator<?> getComparator(final int column){
    		return new Comparator<String>(){
    			/**
    			 * Compares two elements of same column.
    			 * @param arg0	Element number one of the comparing.
    			 * @param arg1	Element number two of the comparing.
    			 * @return		The comparing result.
    			 */
				@Override
				public int compare(String arg0, String arg1) {
					// If actual column is in the integer sorted list
					// convert content and do integer comparing.
					for(int i : intSortedColumns){
						if (column == i){
							Integer i1 = new Integer(Integer.parseInt(arg0));
							Integer i2 = new Integer(Integer.parseInt(arg1));
							return i1.compareTo(i2);
						}
					}
					// Else do normal string comparing.
					return arg0.compareTo(arg1);
				}			
    		};
    	}
    }
    
	private class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 3898500172740758530L;
		private String [] columnNames;
	
		public MyTableModel(String [] names){
				columnNames = names;
		}
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			if(data != null)
				return data.size();
			return 0;
		}

		@Override
		public Object getValueAt(int row, int col) {
			//if (data != null)
				try{
				return data.get(row)[col];
				} catch(Exception e){ return null;}
			//return null;
		}
		@Override
	    public String getColumnName(int col) {
            return columnNames[col];
		}
		@Override
	    public boolean isCellEditable(int rowIndex, int vColIndex) {
	        return false;
	    }
		 
	}

	
	private class ColorRenderer extends JLabel implements TableCellRenderer{

		private static final long serialVersionUID = -6741523493771641674L;
		private String columnName;
	    private CellReplacementRule[] replacementRules;
	     
	    public ColorRenderer(String column, CellReplacementRule[] rules){
	    	columnName = column;
	        replacementRules = rules;
	        setOpaque(true);
	    }
	     
	    @Override
		public Component getTableCellRendererComponent(	JTable table,
														Object value,
														boolean isSelected,
														boolean hasFocus,
														int row,
														int column	) {
	    	int columnValue = 0;
	    	try{
			columnValue=(Integer)table.getValueAt(row,table.getColumnModel()
		    	   				.getColumnIndex(columnName));
	    	} catch(Exception e){e.printStackTrace();}
		      
		
		    // set default Replacement Rule
			CellReplacementRule rule = 
				new CellReplacementRule(0,"ERROR",new Color(255,255,255));
			
			// search for correct Replacement Rule
		    for(CellReplacementRule r : replacementRules){
		    	if(r.realContent == columnValue){
		    		rule = r;
		    		break;
		    	}
		    }
	      
		    // replace Text
		    setText(rule.replacementText);
     
	    	// if not selected change Background Color
	    	if(!isSelected){
	    		setForeground(table.getForeground());
	    		setBackground(rule.backgroundColor);
	    	}
	    	else{
	    		setForeground(table.getForeground());
	    		setBackground(table.getForeground());
	    	}
			return this;
		}
	}	
}