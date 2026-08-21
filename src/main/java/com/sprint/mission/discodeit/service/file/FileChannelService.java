package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileChannelService implements ChannelService {
    private Path path;
    private File[] files;

    public FileChannelService() {
        path = Paths.get("data\\channel");

        try {
            Files.createDirectory(path);
            System.out.println("[초기화 단계] Channel 데이터 저장을 위한 디렉토리가 생성되었습니다.");
        } catch (FileAlreadyExistsException ignored) {

        } catch (NoSuchFileException e) {
            System.out.println("폴더 경로가 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSave(Channel channel) {
        try {
            FileOutputStream fos = new FileOutputStream("data\\channel\\channel-" + channel.getId() + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(channel);
            System.out.println(channel.getChannel() + " [채널] 네임 업데이트 완료.");

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Channel channel) {
        // 파일에 객체 직렬화하기
        try {
            List<Channel> channels = this.readAll();
            if (channels != null) { //users가 존재한다면
                for (Channel c : channels) { //각 users를 user 객체에 넣어서
                    if (c.getId().equals(channel.getId()) || c.getChannel().equals(channel.getChannel())) { //중복인지 검사한다.
                        System.out.println(c.getChannel() + "은(는) 이미 존재하는 채널입니다.");
                        return;
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream("data\\channel\\channel-" + channel.getId() + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(channel);
            System.out.println(channel.getChannel() + " 채널 저장 완료.");

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel read(Channel channel) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Channel c = (Channel) obj;
                if (c.getId().equals(channel.getId())) {
                    return c;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        System.out.println("해당 User를 찾을 수 없습니다.");
        return null;
    }


    @Override
    public List<Channel> readAll() {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<Channel> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((Channel) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return null;
            }
        }

        return result;
    }

    @Override
    public void update(Channel channel, String data) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Channel c = (Channel) obj;
                if (c.getChannel().equals(data)) {
                    System.out.println("동일한 이름의 채널이 이미 존재합니다.");
                    return;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Channel c = (Channel) obj;
                if (c.getId().equals(channel.getId())) {
                    String temp = c.getChannel();
                    c.setChannel(data);
                    c.setUpdatedAt();

                    updateSave(c);

                    System.out.println(temp + " -> " + data + " 로 정상적으로 Update 완료.");
                    return;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }
        System.out.println("해당 User를 찾을 수 없습니다.");
    }

    @Override
    public void delete(Channel channel) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Channel c = (Channel) obj;
                ois.close(); //인풋 스트림을 열어둔 상태에서 delete는 불가능. 사용하고 있는 프로세서가 있는데 삭제하면 안되는 것 처럼. 닫아주고 삭제해야함.
                if (c.getId().equals(channel.getId())) {
                    if (file.delete()) {
                        System.out.println(channel.getChannel() + " Channel 삭제 성공");
                        return;
                    }
                    else {
                        System.out.println("파일을 삭제하지 못했습니다.");
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        System.out.println("[Delete 오류] 해당 User를 찾을 수 없습니다.");
    }
}
