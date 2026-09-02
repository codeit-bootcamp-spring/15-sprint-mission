package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private ChannelRepository channelRepository;
    private final static BasicChannelService instance = new BasicChannelService();
    private BasicChannelService() { }
    public static BasicChannelService getInstance() {
        return instance;
    }
    public void setChannelRepository(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
    @Override
    public Channel create(String name) {
        Channel channel = new Channel(name);
        return channelRepository.save(channel);
    }

    @Override
    public Channel read(UUID id) {
        try {
            return channelRepository.findById(id).get();
        }
        catch (NoSuchElementException e){
            throw new NoSuchElementException("id 없음");
        }
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String name) {
        Channel channel;
        try{
            channel = channelRepository.findById(id).get();
        }
        catch (NoSuchElementException e){
            throw new NoSuchElementException("id 없음");
        }
        finally {}
        channel.update(name);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {

        try {
            channelRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("io 없음");
        }
        channelRepository.deleteById(id);

    }
}
