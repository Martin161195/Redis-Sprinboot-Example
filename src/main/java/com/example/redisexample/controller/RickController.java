package com.example.redisexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/rick")
public class RickController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacterById(@PathVariable("id") Integer id){


        try{

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

            String url = "https://rickandmortyapi.com/api/character/"+id;
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
           ResponseEntity<String> responseEntity =  restTemplate.exchange(url, HttpMethod.GET, null, String.class);

           if(responseEntity.getStatusCodeValue()==200){
               valueOperations.set("rick-"+id, responseEntity.getBody());
           }

            return new ResponseEntity<>(responseEntity.getBody(),headers ,HttpStatus.OK);
        }catch (Exception e){

            return new ResponseEntity<>("ERRROR,", HttpStatus.BAD_REQUEST);
        }

    }
}
