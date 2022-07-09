public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }


    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    @Override
    public int compareTo(PageEntry PE) {
        int tmpPage = page - PE.page;
        int tmpCount = count - PE.count;
        return tmpCount == 0 ? tmpPage : -tmpCount;

    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName = '" + pdfName +
                ", page = " + page +
                ", count = " + count +
                '}' +
                '\n';
    }
    // ???
}
