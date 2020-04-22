package ppj.weather.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ppj.weather.services.WeatherRecordService;

@RestController
public class FileCSVControler {
    private WeatherRecordService temperatureService;

    @Autowired
    public FileCSVControler(WeatherRecordService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @Transactional
    @RequestMapping(path = "/download/{country}/{city}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String country, @PathVariable String city) {

        String content = temperatureService.exportToFile(country, city);

        String filename = city + "-" + country + ".csv";

        HttpHeaders header = new HttpHeaders();
        prepareHeader(header, filename);

        ByteArrayResource resource = new ByteArrayResource(content.getBytes());

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(content.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private void prepareHeader(HttpHeaders httpHeaders, String filename) {
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
    }

    @RequestMapping(path = "/uploadFile/{country}/{city}", method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String country, @PathVariable String city) {
        temperatureService.importFile(file, country, city);
        return ResponseEntity.ok().body(null);
    }
}
