package ru.abolsoft.sseconnect.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.abolsoft.sseconnect.base.entity.BaseEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Desk extends BaseEntity<Long> {
    private String name;
    @Setter
    private Boolean mobile;

    public static Desk create(String name) {
        return new Desk(name, false);
    }

    public static Desk of(Long id, String name, Boolean mobile) {
        var d = new Desk(name, mobile);
        d.setId(id);
        return d;
    }

}
