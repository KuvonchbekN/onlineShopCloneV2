package uz.exadel.session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.exception.SessionNotFoundException;
import uz.exadel.session.payload.ShoppingSessionDto;
import uz.exadel.session.repo.SessionRepo;
import uz.exadel.session.service.SessionService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl implements SessionService {


    private final SessionRepo sessionRepo;

    @Override
    public String create(ShoppingSessionDto shoppingSessionDto) {
        ShoppingSession shoppingSession = new ShoppingSession(null, shoppingSessionDto.getUserId(), shoppingSessionDto.getTotalAmount(),null, Timestamp.valueOf(LocalDateTime.now()));
        ShoppingSession save = sessionRepo.save(shoppingSession);
        return save.getId();
    }

    @Override
    public ShoppingSession  getShoppingSessionByUserId(String userId) {
        Optional<ShoppingSession> byUserId = sessionRepo.findByUserId(userId);
        if (byUserId.isEmpty()){
            throw new SessionNotFoundException("Session for current User is not found");
        }
        return byUserId.get();
    }

    @Override
    public List<ShoppingSession> getList() {
        return sessionRepo.findAll();
    }

    @Override
    public ShoppingSession getById(String id) {
        Optional<ShoppingSession> byId = sessionRepo.findById(id);
        return byId.orElseThrow(()->new SessionNotFoundException("Current session is not found!"));
    }

    @Override
    public void update(ShoppingSessionDto shoppingSessionDto, String id) {
        Optional<ShoppingSession> byId = sessionRepo.findById(id);
        if (byId.isEmpty()){
            throw new SessionNotFoundException("Session with this id is not found!");
        }

        ShoppingSession shoppingSession = byId.get();

        shoppingSession.setTotalAmount(shoppingSessionDto.getTotalAmount());
        sessionRepo.save(shoppingSession);
    }

    @Override
    public void delete(String id) {
        existsById(id);
        sessionRepo.deleteById(id);
    }

    @Override
    public void existsById(String id){
        boolean exists = sessionRepo.existsById(id);
        if (!exists){
            throw new SessionNotFoundException("Session with this id is not found!");
        }
    }

    @Override
    public void deleteSessionWhenUserDeleted(String userId) {
        boolean exists = sessionRepo.existsByUserId(userId);
        if(!exists){
            throw new SessionNotFoundException(String.format("User with id of %s did not have a session!",userId));
        }
        sessionRepo.deleteByUserId(userId);
    }
}
