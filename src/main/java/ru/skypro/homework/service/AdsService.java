package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.*;

import java.io.IOException;

public interface AdsService {
    Ads getAllAds();
    AdDTO addAd(CreateOrUpdateAd createAd, MultipartFile imageFile, String username) throws IOException;
    ExtendedAd getExtendedAd(Integer id);
    void removeAd(Integer id, String username);
    AdDTO updateAd(Integer id, CreateOrUpdateAd updateAd, String username);
    Ads getUserAds(String username);
    byte[] updateAdImage(Integer id, MultipartFile imageFile, String username) throws IOException;
}