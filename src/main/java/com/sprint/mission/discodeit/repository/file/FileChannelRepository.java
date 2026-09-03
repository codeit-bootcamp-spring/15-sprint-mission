package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.io.*;

public class FileChannelRepository implements ChannelRepository{
    // 1.  .ser -> .json 변경
    private final String FILE_PATH = "channel.json";

    // 2. ObjectMapper 선언 (.indentOutput()으로 JSON 정렬)
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // 3. Json파일 읽기 (readAll)
    private List<Channel> readAll(){
        File file = new File(FILE_PATH);
        if(!file.exists() || file.length() == 0) return new ArrayList<>();
        try{
            // Jackson에서 List<Channel> 제네릭 객체를 정확히 복원하기위해 타입레퍼런스 사용
            return objectMapper.readValue(file, new TypeReference<List<Channel>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 4. Json파일 쓰기
    private void writeAll(List<Channel> channels){
        try{
            objectMapper.writeValue(new File(FILE_PATH), channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel save(Channel channel) {
        List<Channel> channels = readAll();
        channels.add(channel);
        writeAll(channels);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return readAll().stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Channel> findAll() { return readAll(); }

    @Override
    public Channel update(UUID id, String channelName) {
        List<Channel> channels = readAll();
        for(Channel c : channels){
            if(c.getId().equals(id)){
                c.updateChannelName(channelName);
                writeAll(channels);
                return c;
            }
        }
        return null;
    }

    @Override
    public Channel delete(UUID id) {
        List<Channel> channels = readAll();
        Channel removed = null;
        Iterator<Channel> it = channels.iterator();
        while(it.hasNext()){
            Channel channel = it.next();
            if(channel.getId().equals(id)){
                removed = channel;
                it.remove();
                writeAll(channels);
            }
        }
        writeAll(channels);
        return removed;
    }
}