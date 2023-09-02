package com.example.checkout;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaveService {

    private final CheckOutRepository checkOutRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    private final ModelMapper modelMapper = new ModelMapper();

    public Long saveCheckOutData(CheckOutDto checkOutDto){
        CheckOutEntity checkOutEntity = saveDatabase(checkOutDto);

        checkOutDto.setCheckOutId(checkOutEntity.getCheckOutId());
        checkOutDto.setCreatedAt(new Date(checkOutEntity.getCreatedAt().getTime()));
        sendToKafka(checkOutDto);

        return checkOutEntity.getCheckOutId();
    }

    private void sendToKafka(CheckOutDto checkOutDto) {
        try{
            String jsonInString = objectMapper.writeValueAsString(checkOutDto);
            KafkaTemplate.send(CHECKOUT_COMPLETE_TOPIC_NAME,jsonInString);
            log.info("success sendToKafka");
        }catch(Exception e){
            log.error("sendToKafka",e);
        }
    }

    private CheckOutEntity saveDatabase(CheckOutDto checkOutDto){
        CheckOutEntity checkOutEntity =modelMapper.map(checkOutDto,CheckOutEntity.class);

        return checkOutRepository.save(checkOutEntity);
    }
}
