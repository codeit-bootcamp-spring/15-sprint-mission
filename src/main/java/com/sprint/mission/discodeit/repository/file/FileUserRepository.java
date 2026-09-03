package com.sprint.mission.discodeit.repository.file;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "user.json";    // 1. .ser -> .json

    // 2. ObjectMapper 선언 (.indentOutput()으로 JSON 정렬)
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // 3. JSON파일 읽기 (readAll)
    private List<User> readAll(){
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0) return new ArrayList<>();
        try{
            // Jackson에서 List<User> 제네릭 객체를 정확히 복원하기위해 타입레퍼런스 사용
            return objectMapper.readValue(file, new TypeReference<List<User>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 4. Json파일 쓰기
    private void writeAll(List<User> users){
        try{
            objectMapper.writeValue(new File(FILE_PATH), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User save(User user) {
        List<User> users = readAll();
        users.add(user);
        writeAll(users);
        return user;
    }

    @Override
    public User findById(UUID id) {
        return readAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return readAll();
    }

    @Override
    public User update(UUID id, String user) {
        List<User> users = readAll();
        for(User u : users){
            if(u.getId().equals(id)){
                u.updateName(user);
                writeAll(users);
                return u;
            }
        }
        return null;
    }

    @Override
    public User delete(UUID id) {
        List<User> users = readAll();
        User removed = null;
        Iterator<User> it = users.iterator();
        while(it.hasNext()){
            User user = it.next();
            if(user.getId().equals(id)){
                removed = user;
                it.remove();
                break;
            }
        }
        writeAll(users);
        return removed;
    }
}