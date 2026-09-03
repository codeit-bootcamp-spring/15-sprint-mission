package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository{
    // 1.  .ser -> .json 변경
    private final String FILE_PATH = "message.json";

    // 2. ObjectMapper 선언 (.indentOutput()으롷 JSON 정렬)
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // 3. JSON파일 읽기 (readAll)
    private List<Message> readAll(){
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0) return new ArrayList<>();
        try{
            // Jackson에서 List<Message> 제네릭 객체를 정확히 복원하기위해 타입레퍼런스 사용
            return objectMapper.readValue(file, new TypeReference<List<Message>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 4. Json파일 쓰기
    private void writeAll(List<Message> messages){
        try{
            objectMapper.writeValue(new File(FILE_PATH), messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message save(Message message) {
        List<Message> messages = readAll();
        messages.add(message);
        writeAll(messages);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return readAll().stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> findAll() { return readAll(); }

    @Override
    public Message update(UUID id, String content) {
        List<Message> messages = readAll();
        for(Message m : messages){
            if(m.getId().equals(id)){
                m.updateContent(content);
                writeAll(messages);
                return m;
            }
        }
        return null;
    }

    @Override
    public Message delete(UUID id) {
        List<Message> messages = readAll();
        Message removed = null;
        Iterator<Message> it = messages.iterator();
        while(it.hasNext()){
            Message message = it.next();
            if(message.getId().equals(id)){
                removed = message;
                it.remove();
                writeAll(messages);
            }
        }
        writeAll(messages);
        return removed;
    }
}
