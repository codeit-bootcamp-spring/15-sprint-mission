package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileMessageService implements MessageService {
    private Path path;
    private File[] files;
    ChannelService channelService;
    UserService userService;

    public FileMessageService() {
        path = Paths.get("data\\message");

        try {
            Files.createDirectory(path);
            System.out.println("[초기화 단계] Message 데이터 저장을 위한 디렉토리가 생성되었습니다.");
        } catch (FileAlreadyExistsException ignored) {

        } catch (NoSuchFileException e) {
            System.out.println("폴더 경로가 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSave(Message message) {
        try {
            FileOutputStream fos = new FileOutputStream("data\\message\\message-" + message.getId() + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(message);

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(Message message) {
        // 파일에 객체 직렬화하기
        try {
            FileOutputStream fos = new FileOutputStream("data\\message\\message-" + message.getId() + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(message);

            oos.close();
            fos.close();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message read(Message message) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Message m = (Message) obj;
                if (m.getId().equals(message.getId())) {
                    return m;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        System.out.println("해당 User를 찾을 수 없습니다.");
        return null;
    }


    @Override
    public List<Message> readAll() {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<Message> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((Message) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return null;
            }
        }

        return result;
    }

    @Override
    public void update(Message message, String data) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Message m = (Message) obj;
                if (m.getId().equals(message.getId())) {
                    m.setMessage(data);
                    m.setUpdatedAt();

                    updateSave(m);

                    System.out.println(m.getUser().getUser() + "의 <Message> 내용 업데이트 완료.");
                    return;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }
        System.out.println("해당 User를 찾을 수 없습니다.");
    }

    @Override
    public void delete(Message message) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Message m = (Message) obj;
                ois.close(); //인풋 스트림을 열어둔 상태에서 delete는 불가능. 사용하고 있는 프로세서가 있는데 삭제하면 안되는 것 처럼. 닫아주고 삭제해야함.
                if (m.getId().equals(message.getId())) {
                    if (file.delete()) {
                        System.out.println(message.getMessage() + " -> Message 삭제 성공");
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

        System.out.println("[Delete 오류] 해당 Message를 찾을 수 없습니다.");
    }
}
