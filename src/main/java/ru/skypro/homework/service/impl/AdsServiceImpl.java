package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository repository;
    private final AdMapper adMapper;

    @Override
    public Ads getAllAds() {
        List<AdDTO> adDTOs = repository.findAll().stream()
                .map(adMapper::adEntityToAdDTO)
                .collect(Collectors.toList());

        Ads response = new Ads();
        response.setCount(adDTOs.size());
        response.setResults(adDTOs);
        return response;
    }

    @Override
    public AdDTO addAd(CreateOrUpdateAd createAd, MultipartFile file) {
        Ad entity = adMapper.createOrUpdateAdToAdEntity(createAd);
        return adMapper.adEntityToAdDTO(repository.save(entity));
    }

    @Override
    public void removeAd(int id) {
        repository.deleteById(id);
    }

    @Override
    public AdDTO updateAds(int id, CreateOrUpdateAd updateAd) {
        Ad existingAd = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such ad found."));
        existingAd.setTitle(updateAd.getTitle());
        existingAd.setPrice(updateAd.getPrice());
        existingAd.setDescription(updateAd.getDescription());
        return adMapper.adEntityToAdDTO(repository.save(existingAd));
    }

    @Override
    public Ads getAdsMe() {
        List<AdDTO> adDTOs = repository.findAll().stream()
                .map(adMapper::adEntityToAdDTO)
                .collect(Collectors.toList());

        Ads response = new Ads();
        response.setCount(adDTOs.size());
        response.setResults(adDTOs);
        return response;
    }

    @Override
    public String updateImage(int id, MultipartFile image) {
        return "";
    }

    @Override
    public AdDTO getAds(int id) {
        return adMapper.adEntityToAdDTO(
                repository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("No such ad found."))
        );
    }
}