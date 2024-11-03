package com.wildermods.autosplitter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.utils.Array;
import com.worldwalkergames.legacy.context.LegacyViewDependencies;
import com.worldwalkergames.ui.popup.IPopUp;
import com.worldwalkergames.ui.popup.PopUpManager;

public class ThreadSafePopUpManager extends PopUpManager {
	
	public ThreadSafePopUpManager(LegacyViewDependencies dependencies) {
		super(dependencies);
	}

	@Override
	public void initialize() {
		synchronized(super.getPopups()) {
			super.initialize();
		}
	}
	
	@Override
	public void pushFront(@NotNull IPopUp popup, boolean skipFadeIn) {
		synchronized(super.getPopups()) {
			super.pushFront(popup, skipFadeIn);
		}
	}
	
	@Override
	public void pushBack(@NotNull IPopUp popup) {
		synchronized(super.getPopups()) {
			super.pushBack(popup);
		}
	}
	
	@Override
	public void removePopUp(IPopUp popup) {
		synchronized(super.getPopups()) {
			super.removePopUp(popup);
		}
	}
	
	@Nullable
	@Override
	public IPopUp getCurrent() {
		synchronized(super.getPopups()) {
			return super.getCurrent();
		}
	}
	
	@Override
	public Array<IPopUp> getPopups() {
		synchronized(super.getPopups()) {
			Array<IPopUp> popups = super.getPopups();
			return new Array<IPopUp>(popups);
		}
	}
	
	@Nullable
	public <T extends IPopUp> T getPopupByClass(@NotNull Class<T> type) {
		synchronized(super.getPopups()) {
			return super.getPopupByClass(type);
		}
	}
	
	@Override
	public void update(int dtMs) {
		synchronized(super.getPopups()) {
			super.update(dtMs);
		}
	}
	
	@Override
	public void after() {
		synchronized(super.getPopups()) {
			super.after();
		}
	}
	
	@Override
	public boolean isEmpty() {
		synchronized(super.getPopups()) {
			return super.isEmpty();
		}
	}
	
	@Override
	public boolean isModal() {
		synchronized(super.getPopups()) {
			return super.isModal();
		}
	}
	
	@Override
	public boolean isFront(IPopUp popup) {
		synchronized(super.getPopups()) {
			return super.isFront(popup);
		}
	}
}
