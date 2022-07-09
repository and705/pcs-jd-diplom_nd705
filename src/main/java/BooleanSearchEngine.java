import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    //???
    private Map<String, Integer> freqs;
    private TreeSet pageEntryList;//Объекты хранятся в отсортированном и возрастающем порядке
    private Map<String, Set<PageEntry>> resultMap;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы


        resultMap = new HashMap<>( );
        File[] filePDF = pdfsDir.listFiles( );
        for (File file : filePDF) { //перебираем файлы
            PdfDocument doc = new PdfDocument(new PdfReader(file));
            //узнать количество страниц в документе
            int  pageCounter = doc.getNumberOfPages( );
            //Сканируя каждый пдф-файл вы перебираете его страницы
            for (int i = 1; i < pageCounter; i++) {
                freqs = new HashMap<>( );
                //получить текст со страницы
                String text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                //разбить текст на слова (а они в этих документах разделены могут быть не только пробелами)
                String[] words = text.split("\\P{IsAlphabetic}+");
                //для каждой страницы извлекаете из неё слова и подсчитываете их количество
                for (String word : words) {
                    if (word.isEmpty( )) {
                        continue;
                    }
                    freqs.put(word.toLowerCase( ), freqs.getOrDefault(word.toLowerCase( ), 0) + 1);
                }
                for (Map.Entry<String, Integer> mapEntry : freqs.entrySet( )) {
                    String mapEntryKey = mapEntry.getKey( );
                    int mapEntryValue = mapEntry.getValue( );
                    PageEntry pageExmp = new PageEntry(file.getName( ), i, mapEntryValue);
                    pageEntryList = new TreeSet( );
                    pageEntryList.add(pageExmp);
                    if (resultMap.isEmpty( )) { //добавляем, если множество пусто
                        resultMap.put(mapEntryKey, pageEntryList);
                    } else if (!resultMap.containsKey(mapEntryKey)) {//добавляем, если нет совпадений
                        resultMap.put(mapEntryKey, pageEntryList);
                    } else {
                        for (Map.Entry<String, Set<PageEntry>> mapPageEntry : resultMap.entrySet( )) {
                            if (mapEntryKey.equals(mapPageEntry.getKey( ))) {
                                Set<PageEntry> page = new TreeSet<>(mapPageEntry.getValue( ));
                                page.add(pageExmp);
                                resultMap.put(mapEntryKey, page);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
            String input = word.toLowerCase( );
            List<PageEntry> sortPage = new ArrayList<>(resultMap.get(input));
            return sortPage;
        //return Collections.emptyList();
    }
}
