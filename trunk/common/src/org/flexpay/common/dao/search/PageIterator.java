package org.flexpay.common.dao.search;

/**
 * calculate page size and count pages and rows on page.
 */
public class PageIterator {

    public static final int countPageInPane = 5;

    public static final int minCountRowsOnPage = 10;
    public static final int maxCountRowsOnPage = 100;

    private int countRows;
    private int countRowsOnPage;
    private int currentPage;
    private int countPages;

    public PageIterator(int countRows, int countRowsOnPage, int currentPage) {
        this.countRows = countRows < 0 ? 0 : countRows;
        this.countRowsOnPage = countRowsOnPage < minCountRowsOnPage ? minCountRowsOnPage :
                (countRowsOnPage > maxCountRowsOnPage ? maxCountRowsOnPage : countRowsOnPage);
        this.countPages = this.countRows / this.countRowsOnPage + (this.countRows % this.countRowsOnPage != 0 ? 1 : 0);
        this.currentPage = currentPage < 1 || this.countPages <= 0 ? 1 : (currentPage > this.countPages ? this.countPages : currentPage);
    }

    public int getCountRows() {
        return countRows;
    }

    public int getCountRowsOnPage() {
        return countRowsOnPage;
    }

    public int getNumberOfCurrentPage() {
        return currentPage;
    }

    public int getCountPages() {
        return countPages;
    }

    public Page getPage(int number) {
        if (number > getCountPages()) {
            return new Page(number, 0, 0);
        }
        if (number == getCountPages()) {
            return new Page(number, (number - 1) * getCountRowsOnPage() + 1, getCountRows());
        }
        return new Page(number, (number - 1) * getCountRowsOnPage() + 1, number * getCountRowsOnPage());
    }

    public int getNumberOfBeginPageInPane() {
        int begin = getNumberOfCurrentPage() - countPageInPane ;
        if ((getCountPages() - getNumberOfCurrentPage()) <= countPageInPane) {
            begin -= countPageInPane - 1 - getCountPages() + getNumberOfCurrentPage();
        }
        if (begin < 1) {
            begin = 1;
        }
        return begin;
    }

    public int getNumberOfEndPageInPane() {
        int end = getNumberOfCurrentPage() + countPageInPane;
        if (getNumberOfCurrentPage() <= countPageInPane) {
            end += countPageInPane - 1 - getNumberOfCurrentPage();
        }
        if (end > getCountPages()) {
            end = getCountPages();
        }
        if (end < 1) {
            end = 1;
        }
        return end;
    }

    public Page getCurrentPage() {
        return getPage(getNumberOfCurrentPage());
    }

}
