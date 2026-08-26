package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileChannelRepository implements ChannelRepository, Serializable {
    private final Path path;

    public FileChannelRepository() {
        Path parentPath = Paths.get("data");
        try {
            Files.createDirectory(parentPath);
        } catch (FileAlreadyExistsException ignored) {

        }
        catch (NoSuchFileException e) {
            System.out.println("폴더 경로가 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }

        path = Paths.get("data\\channels");

        try {
            Files.createDirectory(path);
            System.out.println("[초기화 단계] user 데이터 저장을 위한 디렉토리가 생성되었습니다.");
        } catch (FileAlreadyExistsException ignored) {

        }
        catch (NoSuchFileException e) {
            System.out.println("폴더 경로가 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Channel> readAll() {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<Channel> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new ArrayList<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((Channel) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return new ArrayList<>();
            }
        }

        return result;
    }

    @Override
    public boolean create(Channel channel) {
        //중복 검사는 여기서 안한다고 일단 생각하자.
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("data\\channels\\channel-" + channel.getId() + ".ser")
        )) {
            oos.writeObject(channel);
            System.out.println("["+ channel.getChannelName() + "] 채널 저장 완료");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean update(Channel channel) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream("data\\channels\\channel-" + channel.getId() + ".ser")
                ))
        {
            oos.writeObject(channel);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Channel channel) {
        File file = new File("data\\channels\\channel-" + channel.getId() + ".ser");
        return file.exists() && file.delete();
    }
}
