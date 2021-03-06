/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Gareth Jon Lynch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.gazbert.bxbot.rest.api;

import com.gazbert.bxbot.rest.security.User;
import com.gazbert.bxbot.domain.market.MarketConfig;
import com.gazbert.bxbot.services.MarketConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * TODO Work in progress...
 * <p>
 * Controller for directing Market config requests.
 *
 * @author gazbert
 * @since 1.0
 */
@RestController
@RequestMapping("/api/config")
public class MarketConfigController {

    private final MarketConfigService marketConfigService;

    @Autowired
    public MarketConfigController(MarketConfigService marketConfigService) {
        Assert.notNull(marketConfigService, "marketConfigService dependency cannot be null!");
        this.marketConfigService = marketConfigService;
    }

    @RequestMapping(value = "/market/{marketId}", method = RequestMethod.GET)
    public MarketConfig getMarket(@AuthenticationPrincipal User user, @PathVariable String marketId) {
        return marketConfigService.findById(marketId);
    }

    @RequestMapping(value = "/market/{marketId}", method = RequestMethod.PUT)
    ResponseEntity<?> updateMarket(@AuthenticationPrincipal User user, @PathVariable String marketId, @RequestBody MarketConfig config) {

        final MarketConfig updatedMarketConfig = marketConfigService.updateMarket(config);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{marketId}")
                .buildAndExpand(updatedMarketConfig.getId()).toUri());
        return new ResponseEntity<>(updatedMarketConfig, httpHeaders, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/market/{marketId}", method = RequestMethod.POST)
    ResponseEntity<?> createMarket(@AuthenticationPrincipal User user, @PathVariable String marketId, @RequestBody MarketConfig config) {

        final MarketConfig updatedMarketConfig = marketConfigService.createMarket(config);
        final HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{marketId}")
                .buildAndExpand(updatedMarketConfig.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/market/{marketId}", method = RequestMethod.DELETE)
    public MarketConfig deleteMarket(@AuthenticationPrincipal User user, @PathVariable String marketId) {
        return marketConfigService.deleteMarketById(marketId);
    }

    @RequestMapping(value = "/market", method = RequestMethod.GET)
    public List<MarketConfig> getAllMarkets(@AuthenticationPrincipal User user) {
        return marketConfigService.findAllMarkets();
    }
}

