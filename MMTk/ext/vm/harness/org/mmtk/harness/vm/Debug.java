/*
 *  This file is part of the Jikes RVM project (http://jikesrvm.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License. You
 *  may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  See the COPYRIGHT.txt file distributed with this work for information
 *  regarding copyright ownership.
 */
package org.mmtk.harness.vm;

import org.mmtk.harness.lang.Trace;
import org.mmtk.harness.lang.Trace.Item;
import org.mmtk.harness.sanity.FromSpaceInvariant;
import org.mmtk.plan.Simple;
import org.mmtk.plan.TraceLocal;
import org.vmmagic.unboxed.Address;
import org.vmmagic.unboxed.ObjectReference;
import org.vmmagic.unboxed.harness.Clock;

/**
 * Debugger support for the MMTk harness
 */
public final class Debug extends org.mmtk.vm.Debug {

  /**
   * Enable MMTk debugger support
   */
  @Override
  public boolean isEnabled() {
    return true;
  }

  private String format(ObjectReference obj) {
    if (obj.isNull()) {
      return obj.toString();
    }
    return ObjectModel.getString(obj);
  }

  private String format(Address addr) {
    return ObjectModel.addressAndSpaceString(addr);
  }

  /**
   * @see org.mmtk.vm.Debug#arrayRemsetEntry(org.vmmagic.unboxed.Address, org.vmmagic.unboxed.Address)
   */
  @Override
  public void arrayRemsetEntry(Address start, Address guard) {
    if (Trace.isEnabled(Item.REMSET)) {
      Clock.stop();
      Trace.trace(Item.REMSET, "arrayRemset: [%s,%s)", start, guard);
      Clock.start();
    }
  }

  /**
   * @see org.mmtk.vm.Debug#modbufEntry(org.vmmagic.unboxed.ObjectReference)
   */
  @Override
  public void modbufEntry(ObjectReference object) {
    if (Trace.isEnabled(Item.REMSET)) {
      Clock.stop();
      Trace.trace(Item.REMSET, "modbuf: %s", format(object));
      Clock.start();
    }
  }

  /**
   * @see org.mmtk.vm.Debug#remsetEntry(org.vmmagic.unboxed.Address)
   */
  @Override
  public void remsetEntry(Address slot) {
    if (Trace.isEnabled(Item.REMSET)) {
      Clock.stop();
      Trace.trace(Item.REMSET, "remset: %s->%s", format(slot), format(slot.loadObjectReference()));
      Clock.start();
    }
  }

  /**
   * @see org.mmtk.vm.Debug#globalPhase(short, boolean)
   */
  @Override
  public void globalPhase(short phaseId, boolean before) {
    Clock.stop();
    if (phaseId == Simple.RELEASE && before) {
      new FromSpaceInvariant();
    }
    if (phaseId == Simple.COMPLETE && !before) {
      Scanning.releaseThreads();
    }
    Clock.start();
  }

  /**
   * @see org.mmtk.vm.Debug#traceObject(org.mmtk.plan.TraceLocal, org.vmmagic.unboxed.ObjectReference)
   */
  @Override
  public void traceObject(TraceLocal trace, ObjectReference object) {
    if (Trace.isEnabled(Item.TRACEOBJECT)) {
      Clock.stop();
      Trace.trace(Item.TRACEOBJECT, "traceObject: %s", format(object));
      Clock.start();
    }
  }

  /**
   * Trace insertions at the head of a queue
   * @param value Value inserted
   */
  @Override
  public void queueHeadInsert(String queueName, Address value) {
    if (Trace.isEnabled(Item.QUEUE)) {
      Clock.stop();
      Trace.trace(Item.QUEUE, "head insert %s to %s", value, queueName);
      Clock.start();
    }
  }

  /**
   * Trace removals from the head of a queue
   * @param value Value inserted
   */
  @Override
  public void queueHeadRemove(String queueName, Address value) {
    if (Trace.isEnabled(Item.QUEUE)) {
      Clock.stop();
      Trace.trace(Item.QUEUE, "head remove %s from %s", value, queueName);
      Clock.start();
    }
  }

  /**
   * Trace insertions at the tail of a queue
   * @param value Value inserted
   */
  @Override
  public void queueTailInsert(String queueName, Address value) {
    if (Trace.isEnabled(Item.QUEUE)) {
      Clock.stop();
      Trace.trace(Item.QUEUE, "tail insert %s to %s", value, queueName);
      Clock.start();
    }
  }

  /**
   * Trace removals from the tail of a queue
   * @param value Value removed
   */
  @Override
  public void queueTailRemove(String queueName, Address value) {
    if (Trace.isEnabled(Item.QUEUE)) {
      Clock.stop();
      Trace.trace(Item.QUEUE, "tail remove %s from %s", value, queueName);
      Clock.start();
    }
  }
}
