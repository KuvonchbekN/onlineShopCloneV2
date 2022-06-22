package uz.exadel.session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.exception.SessionNotFoundException;
import uz.exadel.session.payload.ShoppingSessionDto;
import uz.exadel.session.repo.SessionRepo;
import uz.exadel.session.service.SessionService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepo sessionRepo;

    //TODO here I need to make jar of common classes such as mapStruct, ResponseItem, and if possible BaseService which was used in product microservice as well
    @Override
    public String create(ShoppingSessionDto shoppingSessionDto) {
        ShoppingSession shoppingSession = ShoppingSession.builder()
                .userId(shoppingSessionDto.getUserId())
                .totalAmount(shoppingSessionDto.getTotalAmount())
                .build();
        sessionRepo.save(shoppingSession);
        return shoppingSession.getId();
    }

    @Override
    public ShoppingSession getShoppingSessionByUserId(String userId) {
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
        //TODO let's see whether data will get updated even if I don't save it
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
