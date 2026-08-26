package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

public class FileUserRepository implements UserRepository, Serializable {
    private final Path path;

    public FileUserRepository() {
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

        path = Paths.get("data\\users");

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
    public Set<User> readAll() {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        Set<User> result = new HashSet<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new HashSet<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((User) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return new HashSet<>();
            }
        }

        return result;
    }

    @Override
    public boolean create(User user) {
        //중복 검사는 여기서 안한다고 일단 생각하자.
        try (ObjectOutputStream oos = new ObjectOutputStream(
                     new FileOutputStream("data\\users\\user-" + user.getId() + ".ser")
             )) {
            oos.writeObject(user);
            System.out.println("["+ user.getUser() + "] 유저 저장 완료");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean update(User user) {
        if (user == null) {
            System.out.println("User가 정상적이지 않습니다.");
            return false;
        }

        try (
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream("data\\users\\user-" + user.getId() + ".ser")
                ))
        {
            oos.writeObject(user);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        if (user == null) {
            System.out.println("User를 삭제하려고 했으나 삭제하려는 User 데이터가 정상적이지 않습니다. 아마도 Null");
            return false;
        }
        File file = new File("data\\users\\user-" + user.getId() + ".ser");
        return file.exists() && file.delete();
    }
}
