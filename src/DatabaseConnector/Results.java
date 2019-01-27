package DatabaseConnector;

public class Results {

    private String[] columnHeaders;
    private Object[][] table;
    private int rowCounter = 0;
    private int rows;
    private int cols;

    /**
     * @param columns - number of columns
     * @param rows - number of rows
     * Set the number of rows and columns for the dataset
     */
    public Results(int rows, int columns) {
        this.columnHeaders = new String[columns];
        this.table = new Object[rows][columns];
        this.rows = rows;
        this.cols = columns;
    }

    /**
     * @param index - column number
     * @param name - name of the column
     * Set the name of the column in position @param index
     */
    public void setColumn (int index, String name) {
        this.columnHeaders[index] = name;
    }

    /**
     * @param columnHeaders - a String array of the column headers
     * Copies the values from @param columnHeaders into the array of column headers
     */
    public void setColumn(String[] columnHeaders) {
        //need full copy from one arr to the other
        int size = columnHeaders.length;
        this.columnHeaders = new String[size];
        for(int i = 0; i < size; i ++) {
            this.columnHeaders[i] = columnHeaders[i];
        }
    }

    /**
     * @return the string array of column headers
     */
    public String[] getHeaders() {
        return this.columnHeaders;
    }

    /**
     * @return the full dataset (2D array)
     */
    public Object[][] getFullTable() {
        return this.table;
    }

    /**
     * @return number of rows in dataset
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * @return number of columns in dataset
     */
    public int getCols() {
        return this.cols;
    }

    /**
     * @return if the dataset is empty
     */
    public boolean isEmpty() {
        if(this.table == null || this.rows == 0)
            return true;
        return false;
    }

    /**
     * @param index - row number
     * @return the row provided by the @param index
     */
    public Object[] getRow(int index) {
        return this.table[index];
    }

    /**
     * @return the next row of the dataset
     */
    public Object[] getNextRow() {
        this.rowCounter++;
        return this.table[rowCounter-1];
    }

    /**
     * @param col - column number
     * @param row - row number
     * @param obj - object to store
     */
    public void setElement(int row, int col, Object obj) {
        this.table[row][col] = obj;
    }

    public Object getElement(int row, int col) {
        return this.table[row][col];
    }

}
