package com.flightinfo.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.flightinfo.app.data.model.TrackedFlight;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TrackedFlightDao_Impl implements TrackedFlightDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TrackedFlight> __insertionAdapterOfTrackedFlight;

  private final EntityDeletionOrUpdateAdapter<TrackedFlight> __deletionAdapterOfTrackedFlight;

  private final EntityDeletionOrUpdateAdapter<TrackedFlight> __updateAdapterOfTrackedFlight;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTrackedFlightById;

  public TrackedFlightDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrackedFlight = new EntityInsertionAdapter<TrackedFlight>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tracked_flights` (`flightId`,`flightNumber`,`lastStatus`,`lastUpdated`,`notificationEnabled`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrackedFlight entity) {
        if (entity.getFlightId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getFlightId());
        }
        if (entity.getFlightNumber() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFlightNumber());
        }
        if (entity.getLastStatus() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLastStatus());
        }
        statement.bindLong(4, entity.getLastUpdated());
        final int _tmp = entity.getNotificationEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__deletionAdapterOfTrackedFlight = new EntityDeletionOrUpdateAdapter<TrackedFlight>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tracked_flights` WHERE `flightId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrackedFlight entity) {
        if (entity.getFlightId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getFlightId());
        }
      }
    };
    this.__updateAdapterOfTrackedFlight = new EntityDeletionOrUpdateAdapter<TrackedFlight>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tracked_flights` SET `flightId` = ?,`flightNumber` = ?,`lastStatus` = ?,`lastUpdated` = ?,`notificationEnabled` = ? WHERE `flightId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrackedFlight entity) {
        if (entity.getFlightId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getFlightId());
        }
        if (entity.getFlightNumber() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFlightNumber());
        }
        if (entity.getLastStatus() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLastStatus());
        }
        statement.bindLong(4, entity.getLastUpdated());
        final int _tmp = entity.getNotificationEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        if (entity.getFlightId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFlightId());
        }
      }
    };
    this.__preparedStmtOfDeleteTrackedFlightById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tracked_flights WHERE flightId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTrackedFlight(final TrackedFlight trackedFlight,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTrackedFlight.insert(trackedFlight);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTrackedFlight(final TrackedFlight trackedFlight,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTrackedFlight.handle(trackedFlight);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTrackedFlight(final TrackedFlight trackedFlight,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTrackedFlight.handle(trackedFlight);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTrackedFlightById(final String flightId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTrackedFlightById.acquire();
        int _argIndex = 1;
        if (flightId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, flightId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTrackedFlightById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TrackedFlight>> getAllTrackedFlights() {
    final String _sql = "SELECT * FROM tracked_flights";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tracked_flights"}, new Callable<List<TrackedFlight>>() {
      @Override
      @NonNull
      public List<TrackedFlight> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFlightId = CursorUtil.getColumnIndexOrThrow(_cursor, "flightId");
          final int _cursorIndexOfFlightNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "flightNumber");
          final int _cursorIndexOfLastStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStatus");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfNotificationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationEnabled");
          final List<TrackedFlight> _result = new ArrayList<TrackedFlight>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackedFlight _item;
            final String _tmpFlightId;
            if (_cursor.isNull(_cursorIndexOfFlightId)) {
              _tmpFlightId = null;
            } else {
              _tmpFlightId = _cursor.getString(_cursorIndexOfFlightId);
            }
            final String _tmpFlightNumber;
            if (_cursor.isNull(_cursorIndexOfFlightNumber)) {
              _tmpFlightNumber = null;
            } else {
              _tmpFlightNumber = _cursor.getString(_cursorIndexOfFlightNumber);
            }
            final String _tmpLastStatus;
            if (_cursor.isNull(_cursorIndexOfLastStatus)) {
              _tmpLastStatus = null;
            } else {
              _tmpLastStatus = _cursor.getString(_cursorIndexOfLastStatus);
            }
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final boolean _tmpNotificationEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNotificationEnabled);
            _tmpNotificationEnabled = _tmp != 0;
            _item = new TrackedFlight(_tmpFlightId,_tmpFlightNumber,_tmpLastStatus,_tmpLastUpdated,_tmpNotificationEnabled);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTrackedFlightById(final String flightId,
      final Continuation<? super TrackedFlight> $completion) {
    final String _sql = "SELECT * FROM tracked_flights WHERE flightId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (flightId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, flightId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TrackedFlight>() {
      @Override
      @Nullable
      public TrackedFlight call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFlightId = CursorUtil.getColumnIndexOrThrow(_cursor, "flightId");
          final int _cursorIndexOfFlightNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "flightNumber");
          final int _cursorIndexOfLastStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStatus");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfNotificationEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationEnabled");
          final TrackedFlight _result;
          if (_cursor.moveToFirst()) {
            final String _tmpFlightId;
            if (_cursor.isNull(_cursorIndexOfFlightId)) {
              _tmpFlightId = null;
            } else {
              _tmpFlightId = _cursor.getString(_cursorIndexOfFlightId);
            }
            final String _tmpFlightNumber;
            if (_cursor.isNull(_cursorIndexOfFlightNumber)) {
              _tmpFlightNumber = null;
            } else {
              _tmpFlightNumber = _cursor.getString(_cursorIndexOfFlightNumber);
            }
            final String _tmpLastStatus;
            if (_cursor.isNull(_cursorIndexOfLastStatus)) {
              _tmpLastStatus = null;
            } else {
              _tmpLastStatus = _cursor.getString(_cursorIndexOfLastStatus);
            }
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final boolean _tmpNotificationEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNotificationEnabled);
            _tmpNotificationEnabled = _tmp != 0;
            _result = new TrackedFlight(_tmpFlightId,_tmpFlightNumber,_tmpLastStatus,_tmpLastUpdated,_tmpNotificationEnabled);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
