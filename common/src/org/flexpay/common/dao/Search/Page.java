package org.flexpay.common.dao.search;

public class Page {

    private int startRowOnPage;
    private int endRowOnPage;
    private int number;

    public Page(int number, int startRowOnPage, int endRowOnPage) {
        this.number = number;
        this.startRowOnPage = startRowOnPage;
        this.endRowOnPage = endRowOnPage;
    }

    public int getStartRowOnPage() {
        return startRowOnPage;
    }

    public int getEndRowOnPage() {
        return endRowOnPage;
    }

    public int getNumber() {
        return number;
    }

}
