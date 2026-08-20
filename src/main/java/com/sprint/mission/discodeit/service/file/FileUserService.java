package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

public class FileUserService implements UserService, Serializable {
    private Path path;
    private File[] files;

    public FileUserService() {
        path = Paths.get("user");

        try {
            Files.createDirectory(path);
            System.out.println("[초기화 단계] user 데이터 저장을 위한 디렉토리가 생성되었습니다.");
        } catch (NoSuchFileException e) {
            System.out.println("폴더 경로가 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void create(User user) {
        // 파일에 객체 직렬화하기
        try {
            Set<User> users = this.readAll();
            if (users != null) { //users가 존재한다면
                for (User u : users) { //각 users를 user 객체에 넣어서
                    if (u.getId().equals(user.getId()) || u.getUser().equals(user.getUser())) { //중복인지 검사한다.
                        System.out.println(u.getUser() + "는 이미 존재하는 유저입니다.");
                        return;
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream("user\\user-" + user.getId() + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(user);
            System.out.println(user.getUser() + " 유저 저장 완료.");

            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User read(User user) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                User u = (User) obj;
                if (u.getId().equals(user.getId())) {
                    return u;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        System.out.println("해당 User를 찾을 수 없습니다.");
        return null;
    }


    @Override
    public Set<User> readAll() {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        Set<User> result = new HashSet<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((User) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return null;
            }
        }

        return result;
    }

    @Override
    public void update(User user, String data) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                User u = (User) obj;
                if (u.getId().equals(user.getId())) {
                    String temp = u.getUser();
                    u.setUser(data);
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
    public void delete(User user) {
        files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return;
        }
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                User u = (User) obj;
                if (u.getId().equals(user.getId())) {
                    file.delete();
                    System.out.println(user.getUser() + " User 삭제 성공");
                    return;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        System.out.println("[Delete 오류] 해당 User를 찾을 수 없습니다.");
    }
}
