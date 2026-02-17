package com.rentaroom.repository;

import com.rentaroom.model.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final EntityManager entityManager;

    public RoomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BigDecimal getAveragePrice() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<Room> root = query.from(Room.class);

        Expression<Double> avgExpression = cb.avg(root.get("price").as(Double.class));
        query.select(avgExpression);

        Double result = entityManager.createQuery(query).getSingleResult();
        return result != null ? BigDecimal.valueOf(result) : null;
    }

    @Override
    public List<Object[]> countRoomsByLocation() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Room> root = query.from(Room.class);

        query.multiselect(
            root.get("location"),
            cb.count(root)
        );
        query.groupBy(root.get("location"));
        query.orderBy(cb.desc(cb.count(root)));

        return entityManager.createQuery(query).getResultList();
    }
}
