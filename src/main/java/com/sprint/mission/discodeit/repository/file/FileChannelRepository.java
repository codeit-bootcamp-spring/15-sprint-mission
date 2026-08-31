package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public FileChannelRepository() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        // 파일로 객체로 만들기 위해 데이터 스트림화 한다.
        try (FileOutputStream fos = new FileOutputStream("channel.ser"); // 저장 로직 (channel.ser이라는 실제 파일 아웃픗 만듦)
             ObjectOutputStream oos = new ObjectOutputStream(fos); // channel.ser이라는 아웃풋의 주소값을 변수에 보관한다.
        ) {
            oos.writeObject(data); // data 컬렉션 자체를 객체로 직렬화해 channel.ser 파일에 기록한다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel load(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> loadValue() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = data.remove(id, data.get(id));
        try (FileOutputStream fos = new FileOutputStream("channel.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.remove(id, data.get(id));
    }
}
