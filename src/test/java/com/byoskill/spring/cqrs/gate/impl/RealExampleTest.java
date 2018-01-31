package com.byoskill.spring.cqrs.gate.impl;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.byoskill.spring.cqrs.gate.api.Gate;
import com.byoskill.spring.cqrs.gate.impl.fakeapp.RandomErrorNumber;
import com.byoskill.spring.cqrs.gate.impl.fakeapp.RandomNumber;
import com.byoskill.spring.cqrs.gate.impl.fakeapp.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
@ActiveProfiles("guava_bus")
public class RealExampleTest {


    @Autowired
    private Gate gate;

    @Test
    public void testGateDispatchAll() throws Exception {
	Assert.assertNotNull(gate.dispatch(new RandomNumber(), Integer.class));

	final List<RandomNumber> commands = IntStream.range(0, 20).mapToObj(it -> new RandomNumber()).collect(Collectors.toList());
	final List<Integer> results = gate.dispatchAll(commands, Integer.class);
	Assert.assertEquals(20, results.size());
    }

    @Test(expected=CompletionException.class)
    public void testGateDispatchAllRandomErrors() throws Exception {
	Assert.assertNotNull(gate.dispatch(new RandomNumber(), Integer.class));

	final List<RandomErrorNumber> commands = IntStream.range(0, 20).mapToObj(it -> new RandomErrorNumber()).collect(Collectors.toList());
	final List<Integer> results = gate.dispatchAll(commands, Integer.class);
	Assert.assertEquals(20, results.size());
    }

}
