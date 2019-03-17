package com.lottery.web.cucumber.stepdefs;

import com.lottery.web.LotteryApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = LotteryApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
