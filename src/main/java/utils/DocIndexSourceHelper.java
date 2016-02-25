package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.docsearch.domain.DocIndexSourceEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lyz on 2/25/16.
 */
public class DocIndexSourceHelper {
    public static String DOC_INDEX_FILE = "doc.md";
    public static String DOC_INDEX_FILETITLE = "charpter.md";
    public static String DOC_PATH1 = "/home/lyz/program/doc-helpcenter";
    public static String DOC_PATH = "/home/lyz/Downloads/daocloud-docs-master/pages";
    public static String DOC_TAG_TITLE = "#";
    public static String CHARPTER_TAG_TITLE = "title:";

    public static List<String> listDocTypeName(Path path) throws IOException {
        return Files.walk(path, 2).filter(x -> x.getFileName().toString().endsWith("chapter.md"))
                .map(x -> x.getParent().getFileName().toString()).distinct().sorted().collect(Collectors.toList());
    }

    public static List<DocIndexSourceEntity> listDocEntries(Path path) throws IOException {
        Function<? super Path, String> keymapper = p -> {
            return p.getParent().toString();

        };
        Function<? super Path, String> chaptervaluemapper = p -> {
            try {
                return Files.lines(p).filter(x -> x.startsWith("#")).map(x -> x.replace('#', ' ').trim())
                        .findFirst().orElse("");
            } catch (IOException e) {
                return null;
            }
        };
        Function<? super Path, String> dockeymapper = p -> {
            return p.toString();

        };
        Function<? super Path, String> docvaluemapper = p -> {
            try {
                return Files.lines(p).filter(x -> x.startsWith("title")).map(x -> x.replaceAll("title:", "").trim())
                        .findFirst().orElse("");
            } catch (IOException e) {
                return null;
            }
        };

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        Map<String, String> titlemap = Files.walk(path, 2).filter(x -> x.getFileName().toString().endsWith("chapter.md"))
                .collect(Collectors.toMap(keymapper, chaptervaluemapper, (x, y) -> x)).entrySet().stream()
                .filter(x -> x.getValue() != null && !x.getValue().isEmpty()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(gson.toJson(titlemap));
        Function<? super DocIndexSourceEntity, String> typeMapper = p -> {
            return Paths.get(p.getPath()).getParent().getFileName().toString();
        };
        Function<? super DocIndexSourceEntity, DocIndexSourceEntity> docEntityMapper = p -> {
            return p;
        };
        Map<String, DocIndexSourceEntity> mapEntries = titlemap.entrySet().stream().flatMap(x
                        ->
                {
                    try {
                        return Files.walk(Paths.get(x.getKey()), 2).filter(y -> y.getFileName().toString().endsWith("docs.md"))
                                .collect(Collectors.toMap(dockeymapper, docvaluemapper, (m, n) -> m)).entrySet().stream()
                                .filter(m -> m.getValue() != null && !m.getValue().isEmpty()).map(m -> {
                                    DocIndexSourceEntity entity = new DocIndexSourceEntity();
                                    try {
                                        byte[] base64str = Base64Utils.encodeToBase64(Paths.get(m.getKey()));
                                        entity.setContentBase64(new String(base64str));
                                        entity.setTitle(m.getValue());
                                        entity.setParentTitle(x.getValue());
                                        entity.setPath(m.getKey());
                                        entity.setIndexDateTime(LocalDateTime.now().toString());
                                        return entity;
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }).filter(Objects::nonNull);
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                }
        ).collect(Collectors.toMap(typeMapper, docEntityMapper));

        List<DocIndexSourceEntity> list = titlemap.entrySet().stream().flatMap(x -> {
            try {
                return Files.walk(Paths.get(x.getKey()), 2).filter(y -> y.getFileName().toString().endsWith("docs.md"))
                        .collect(Collectors.toMap(dockeymapper, docvaluemapper, (m, n) -> m)).entrySet().stream()
                        .filter(m -> m.getValue() != null && !m.getValue().isEmpty()).map(m -> {
                            DocIndexSourceEntity entity = new DocIndexSourceEntity();
                            try {
                                byte[] base64str = Base64Utils.encodeToBase64(Paths.get(m.getKey()));
                                entity.setContentBase64(new String(base64str));
                                entity.setTitle(m.getValue());
                                entity.setParentTitle(x.getValue());
                                entity.setPath(m.getKey());
                                entity.setIndexDateTime(LocalDateTime.now().toString());
                                return entity;
                            } catch (Exception e) {
                                return null;
                            }
                        }).filter(Objects::nonNull);
            } catch (IOException e) {
                return Stream.empty();
            }
        }).collect(Collectors.toList());
        return list;
    }

    public static void main(String[] args) throws IOException {
//        listDocEntries(Paths.get(DOC_PATH));
        System.out.println(listDocTypeName(Paths.get(DOC_PATH)));
    }


    public static Map<String, String> getIndexFile(String path) throws IOException {


        LinkedList<String> folderList = new LinkedList<String>();
        folderList.add(path);
        Map<String, String> fileList = new HashMap<>();
        while (folderList.size() > 0) {

            File file = new File(folderList.peek());
            folderList.removeFirst();
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
//                    if (Stream.of(files[i].listFiles())
//                            )listFiles
                    folderList.add(files[i].getPath());
                } else {

                    fileList.put(files[i].getName(), new String(Base64Utils.encodeToBase64(files[i].toPath())));
                }
            }

        }
        return fileList;
    }

}
