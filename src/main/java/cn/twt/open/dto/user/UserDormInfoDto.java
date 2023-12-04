package cn.twt.open.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDormInfoDto {



    private String zone;

    private String building;

    private String floor;

    private String room;

    private String bed;

    public void setNotFound(String notFound){
        this.bed=notFound;
        this.building=notFound;
        this.floor=notFound;
        this.room=notFound;
        this.zone=notFound;
    }

}
