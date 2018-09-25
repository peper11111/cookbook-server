package pl.edu.pw.ee.cookbookserver.helper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.Properties;
import pl.edu.pw.ee.cookbookserver.misc.PayloadKey;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.misc.SortType;

import java.util.Comparator;
import java.util.stream.Stream;

@Component
public class StreamHelper {

    private PayloadHelper payloadHelper;
    private Properties properties;

    @Autowired
    public StreamHelper(PayloadHelper payloadHelper, Properties properties) {
        this.payloadHelper = payloadHelper;
        this.properties = properties;
    }

    public Stream applySorting(JSONObject payload, Stream stream, Comparator comparator) throws ProcessingException {
        SortType sortType = payload.has(PayloadKey.SORT.value()) ? payloadHelper.getValidSortType(payload) : SortType.DESC;
        if (sortType == SortType.ASC) {
            return stream.sorted(comparator);
        } else {
            return stream.sorted(comparator.reversed());
        }
    }

    public Stream applyPagination(JSONObject payload, Stream stream) throws ProcessingException {
        long page = payload.has(PayloadKey.PAGE.value()) ? payloadHelper.getValidPage(payload) : 1;
        return stream.skip(properties.getPageSize() * (page - 1)).limit(properties.getPageSize());
    }
}
