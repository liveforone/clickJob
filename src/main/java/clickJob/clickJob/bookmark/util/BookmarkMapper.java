package clickJob.clickJob.bookmark.util;

import clickJob.clickJob.bookmark.model.Bookmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkMapper {

    //== entity -> map id & title ==//
    public static Map<String, Object> entityToMap(List<Bookmark> bookmarkList) {
        Map<String, Object> map = new HashMap<>();
        List<Long> jobId = new ArrayList<>();
        List<String> jobTitle = new ArrayList<>();

        for (Bookmark bookmark : bookmarkList) {
            jobId.add(bookmark.getJob().getId());
            jobTitle.add(bookmark.getJob().getTitle());
        }

        map.put("jobId", jobId);
        map.put("jobTitle", jobTitle);

        return map;
    }
}
